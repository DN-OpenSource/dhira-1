package dhira1

import chisel3._

/** MAC unit (compute atom)
  *
  * Spec:
  *  - 16-bit multiply
  *  - 32-bit accumulate
  *  - pipelined
  *
  * Pipeline:
  *  Stage 1: multiply
  *  Stage 2: add
  *  Stage 3: register output
  */
class MacUnit extends Module {
  val io = IO(new Bundle {
    val validIn = Input(Bool())

    val a = Input(SInt(16.W))
    val b = Input(SInt(16.W))
    val accIn = Input(SInt(32.W))

    val validOut = Output(Bool())
    val y = Output(SInt(32.W))
  })

  // Stage 1: multiply (keep full precision, 32-bit)
  val s1_valid = RegNext(io.validIn, init = false.B)
  val s1_mul = Reg(SInt(32.W))
  when(io.validIn) {
    s1_mul := (io.a * io.b).asSInt
  }

  // Stage 2: add
  val s2_valid = RegNext(s1_valid, init = false.B)
  val s2_add = Reg(SInt(32.W))
  val s1_acc = Reg(SInt(32.W))
  when(io.validIn) {
    // align accIn with mul in pipeline
    s1_acc := io.accIn
  }
  when(s1_valid) {
    s2_add := (s1_mul + s1_acc).asSInt
  }

  // Stage 3: register output
  val s3_valid = RegNext(s2_valid, init = false.B)
  val s3_out = Reg(SInt(32.W))
  when(s2_valid) {
    s3_out := s2_add
  }

  io.validOut := s3_valid
  io.y := s3_out
}
