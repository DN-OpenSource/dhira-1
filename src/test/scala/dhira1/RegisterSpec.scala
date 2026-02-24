package dhira1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class RegisterSpec extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "SimpleRegister"

  it should "hold value when disabled and dump VCD" in {
    test(new SimpleRegister(8))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { c =>
        c.io.en.poke(false.B)
        c.io.d.poke(10.U)
        c.clock.step(1)
        c.io.q.expect(0.U)

        c.io.en.poke(true.B)
        c.clock.step(1)
        c.io.q.expect(10.U)

        c.io.en.poke(false.B)
        c.io.d.poke(33.U)
        c.clock.step(2)
        c.io.q.expect(10.U)
      }
  }
}
