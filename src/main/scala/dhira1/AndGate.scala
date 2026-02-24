package dhira1

import chisel3._

class AndGate extends Module {
  val io = IO(new Bundle {
    val a = Input(Bool())
    val b = Input(Bool())
    val y = Output(Bool())
  })

  io.y := io.a & io.b
}
