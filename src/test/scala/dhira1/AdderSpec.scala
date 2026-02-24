package dhira1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class AdderSpec extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Adder"

  it should "add two numbers and dump VCD" in {
    test(new Adder(8))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { c =>
        c.io.a.poke(3.U)
        c.io.b.poke(4.U)
        c.clock.step(1)
        c.io.y.expect(7.U)

        c.io.a.poke(255.U)
        c.io.b.poke(1.U)
        c.clock.step(1)
        c.io.y.expect(256.U)
      }
  }
}
