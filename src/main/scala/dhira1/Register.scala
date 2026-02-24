package dhira1

import chisel3._

class SimpleRegister(width: Int = 8) extends Module {
  val io = IO(new Bundle {
    val d = Input(UInt(width.W))
    val en = Input(Bool())
    val q = Output(UInt(width.W))
  })

  val r = RegInit(0.U(width.W))
  when(io.en) {
    r := io.d
  }
  io.q := r
}
