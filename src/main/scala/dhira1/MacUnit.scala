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

  // Valid pipeline (explicit RegInit avoids backend init quirks)
  val v1 = RegInit(false.B)
  val v2 = RegInit(false.B)
  val v3 = RegInit(false.B)
  v1 := io.validIn
  v2 := v1
  v3 := v2
  io.validOut := v3

  // Stage 1: multiply + align acc
  val s1_mul = RegInit(0.S(32.W))
  val s1_acc = RegInit(0.S(32.W))
  s1_mul := (io.a * io.b).asSInt
  s1_acc := io.accIn

  // Stage 2: add
  val s2_add = RegInit(0.S(32.W))
  s2_add := (s1_mul + s1_acc).asSInt

  // Stage 3: register output
  val s3_out = RegInit(0.S(32.W))
  s3_out := s2_add

  io.y := s3_out
}
