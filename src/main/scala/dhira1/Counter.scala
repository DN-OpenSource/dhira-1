package dhira1

import chisel3._

class Counter(width: Int = 8) extends Module {
  val io = IO(new Bundle {
    val en = Input(Bool())
    val value = Output(UInt(width.W))
  })

  val r = RegInit(0.U(width.W))
  when(io.en) {
    r := r + 1.U
  }
  io.value := r
}
