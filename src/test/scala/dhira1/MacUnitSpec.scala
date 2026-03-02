package dhira1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class MacUnitSpec extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "MacUnit"

  private def clip16(x: Int): Int = {
    // signed 16-bit
    val y = x & 0xFFFF
    if ((y & 0x8000) != 0) y - 0x10000 else y
  }

  private def clip32(x: Long): Int = {
    // signed 32-bit
    val y = x & 0xFFFFFFFFL
    if ((y & 0x80000000L) != 0) (y - 0x100000000L).toInt else y.toInt
  }

  it should "match software reference for random vectors (pipeline latency 3) and dump VCD" in {
    test(new MacUnit)
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { c =>

        val rnd = new Random(1)
        val N = 200

        // expected outputs queued by cycle
        val exp = collection.mutable.Queue[Option[Int]]()

        // init
        c.io.validIn.poke(false.B)
        c.io.a.poke(0.S)
        c.io.b.poke(0.S)
        c.io.accIn.poke(0.S)

        // settle after reset
        c.clock.step(1)

        // prime exp with 3 bubbles (pipeline latency)
        exp.enqueue(None)
        exp.enqueue(None)
        exp.enqueue(None)

        for (_ <- 0 until N) {
          val a = clip16(rnd.nextInt())
          val b = clip16(rnd.nextInt())
          val acc = clip32(rnd.nextLong())

          val y = clip32(a.toLong * b.toLong + acc.toLong)

          c.io.validIn.poke(true.B)
          c.io.a.poke(a.S(16.W))
          c.io.b.poke(b.S(16.W))
          c.io.accIn.poke(acc.S(32.W))

          exp.enqueue(Some(y))

          // step and check output for this cycle
          c.clock.step(1)
          val want = exp.dequeue()
          want match {
            case Some(w) =>
              c.io.validOut.expect(true.B)
              c.io.y.expect(w.S(32.W))
            case None =>
              c.io.validOut.expect(false.B)
          }
        }

        // drain pipeline
        c.io.validIn.poke(false.B)
        for (_ <- 0 until 6) {
          exp.enqueue(None)
          c.clock.step(1)
          val want = exp.dequeue()
          want match {
            case Some(w) =>
              c.io.validOut.expect(true.B)
              c.io.y.expect(w.S(32.W))
            case None =>
              c.io.validOut.expect(false.B)
          }
        }
      }
  }

  it should "compute a bit-accurate dot product (accumulate externally)" in {
    test(new MacUnit)
      .withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>

        val vecA = Seq(1, -2, 3, -4, 5, -6, 7, -8).map(_.toInt)
        val vecB = Seq(2, 3, -1, 4, -2, 1, 2, -3).map(_.toInt)

        def refDot: Int = {
          val s = vecA.zip(vecB).map { case (a, b) => a.toLong * b.toLong }.sum
          clip32(s)
        }

        // feed MAC with accIn=0 each time, and accumulate outputs in software after pipeline
        val exp = collection.mutable.Queue[Option[Int]]()

        c.io.validIn.poke(false.B)
        c.io.a.poke(0.S)
        c.io.b.poke(0.S)
        c.io.accIn.poke(0.S)

        // settle
        c.clock.step(1)

        exp.enqueue(None); exp.enqueue(None); exp.enqueue(None)

        var running: Long = 0

        for ((a0, b0) <- vecA.zip(vecB)) {
          val a = clip16(a0)
          val b = clip16(b0)
          val y = clip32(a.toLong * b.toLong + 0L)
          exp.enqueue(Some(y))

          c.io.validIn.poke(true.B)
          c.io.a.poke(a.S(16.W))
          c.io.b.poke(b.S(16.W))
          c.io.accIn.poke(0.S(32.W))

          c.clock.step(1)
          val got = exp.dequeue()
          got match {
            case Some(_) =>
              c.io.validOut.expect(true.B)
              val term = c.io.y.peek().litValue.toLong
              val signed = if ((term & (1L << 31)) != 0) (term - (1L << 32)) else term
              running += signed
            case None =>
              c.io.validOut.expect(false.B)
          }
        }

        c.io.validIn.poke(false.B)
        for (_ <- 0 until 6) {
          exp.enqueue(None)
          c.clock.step(1)
          val got = exp.dequeue()
          got match {
            case Some(_) =>
              c.io.validOut.expect(true.B)
              val term = c.io.y.peek().litValue.toLong
              val signed = if ((term & (1L << 31)) != 0) (term - (1L << 32)) else term
              running += signed
            case None =>
              c.io.validOut.expect(false.B)
          }
        }

        assert(clip32(running) == refDot)
      }
  }
}
