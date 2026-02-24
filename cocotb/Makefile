TOPLEVEL_LANG = verilog
SIM ?= verilator

# default to repo root out/
OUTDIR ?= ../out/vcd

COCOTB_REDUCED_LOG_FMT=1

# target selection

.PHONY: and_gate adder

and_gate:
	$(MAKE) MODULE=and_gate_test TOPLEVEL=AndGate TOPLEVEL_LIBRARY=AndGate verilog

adder:
	$(MAKE) MODULE=adder_test TOPLEVEL=Adder TOPLEVEL_LIBRARY=Adder verilog

# generic rule
verilog:
	@echo "Run with specific targets (and_gate/adder)."

