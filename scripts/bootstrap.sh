#!/usr/bin/env bash
set -euo pipefail

echo "[dhira-1] Installing toolchain (Ubuntu 22.04)..."

sudo apt-get update -y

# Java + sbt prereqs
sudo apt-get install -y curl gnupg ca-certificates software-properties-common
sudo apt-get install -y openjdk-17-jdk

# sbt repo
if ! grep -q "scala-sbt" /etc/apt/sources.list.d/sbt.list 2>/dev/null; then
  echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
  echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
  curl -fsSL https://keyserver.ubuntu.com/pks/lookup?op=get\&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823 | sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/sbt.gpg
fi

sudo apt-get update -y
sudo apt-get install -y sbt

# RTL sim + waves
sudo apt-get install -y verilator gtkwave

# Python + cocotb
sudo apt-get install -y python3 python3-venv python3-pip make
python3 -m venv .venv
source .venv/bin/activate
pip install -U pip
pip install cocotb numpy

echo "[dhira-1] Done. Next: ./scripts/test_all.sh"
