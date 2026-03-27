#!/usr/bin/env bash
TARGET="$1"
cmd="ping -c 1 $TARGET"
bash -c "$cmd"
