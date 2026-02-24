import cocotb
from cocotb.triggers import Timer


@cocotb.test()
async def truth_table(dut):
    cases = [
        (0, 0, 0),
        (0, 1, 0),
        (1, 0, 0),
        (1, 1, 1),
    ]
    for a, b, y in cases:
        dut.a.value = a
        dut.b.value = b
        await Timer(1, units="ns")
        assert int(dut.y.value) == y
