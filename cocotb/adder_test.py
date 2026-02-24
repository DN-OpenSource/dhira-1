import cocotb
from cocotb.triggers import Timer


@cocotb.test()
async def adds(dut):
    vectors = [
        (3, 4, 7),
        (255, 1, 256),
    ]
    for a, b, y in vectors:
        dut.a.value = a
        dut.b.value = b
        await Timer(1, units="ns")
        assert int(dut.y.value) == y
