# cocotb reference tests (Phase 2)

These tests are a **reference layer**. They are optional for the Scala test flow.

## How to run

After running `./scripts/bootstrap.sh`:

```bash
source .venv/bin/activate
cd cocotb
make and_gate
make adder
```

Waveforms are written as `*.vcd` in `out/vcd/` (repo root).
