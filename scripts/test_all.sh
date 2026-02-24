#!/usr/bin/env bash
set -euo pipefail

echo "[dhira-1] Running Scala/Chisel tests (Verilator + VCD)..."

# Ensure output is consistent
mkdir -p out/vcd

# ChiselTest writes VCDs under test_run_dir by default.
# We keep them in the repo but allow the user to locate them.
# For now, just run tests; waveforms can be found under test_run_dir in target/.

sbt -v test

echo "[dhira-1] Tests complete. If you used VerilatorBackendAnnotation+WriteVcdAnnotation, VCDs are under target/test_run_dir/**/"
