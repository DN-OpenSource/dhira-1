package dhira1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class AndGateSpec extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "AndGate"

  it should "compute AND truth table and dump VCD" in {
    test(new AndGate)
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { c =>
        val cases = Seq(
          (false, false, false),
          (false, true, false),
          (true, false, false),
          (true, true, true)
        )
        for ((a, b, y) <- cases) {
          c.io.a.poke(a.B)
          c.io.b.poke(b.B)
          c.clock.step(1)
          c.io.y.expect(y.B)
        }
      }
  }
}
