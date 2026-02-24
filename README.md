# DHIRA-1 — AI-Native Transformer Inference Accelerator (Chisel)

This repository implements **DHIRA-1**, a bottom-up build of an AI-native transformer inference accelerator.

## Execution principles
1) Build bottom-up
2) Simulate every module
3) Never integrate unverified RTL
4) Pipeline all arithmetic
5) Keep compute fed
6) Design memory first-class
7) Optimize dataflow, not instructions

## Roadmap
See: **DHIRA-1 — ZERO → GEN-2 Scaling Execution Roadmap** (internal doc).

## Requirements (Ubuntu 22.04)
- JDK (Temurin/OpenJDK 17)
- sbt
- Verilator
- GTKWave
- Python 3 + venv
- cocotb + numpy

## Quickstart

### 1) Bootstrap env
```bash
./scripts/bootstrap.sh
```

### 2) Run Phase-2 tests (logic foundations)
```bash
./scripts/test_all.sh
```

### 3) View waveforms
Waveforms are written under `out/vcd/`.

Example:
```bash
gtkwave out/vcd/and_gate.vcd
```

## Repo layout
- `build.sbt` — Scala/Chisel build
- `src/main/scala/dhira1/` — RTL
- `src/test/scala/dhira1/` — Scala testbenches
- `cocotb/` — Python reference tests (cocotb)
- `scripts/` — bootstrap + test runners
- `out/` — generated artifacts (ignored)
