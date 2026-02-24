package dhira1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CounterSpec extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Counter"

  it should "increment when enabled and dump VCD" in {
    test(new Counter(8))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { c =>
        c.io.en.poke(false.B)
        c.clock.step(2)
        c.io.value.expect(0.U)

        c.io.en.poke(true.B)
        c.clock.step(5)
        c.io.value.expect(5.U)
      }
  }
}
