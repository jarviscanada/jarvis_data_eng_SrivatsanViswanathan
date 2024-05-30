#!/bin/bash

# Hostname
hostname=$(hostname -f)
echo "Hostname:" $hostname

# Hardware Info
lscpu_out=`lscpu`

cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{ $1=$2=""; print substr($0,3) }' | xargs)
cpu_mhz=$(grep "MHz" /proc/cpuinfo | awk '{print $4}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "L2 cache" | awk '{print $3}' | xargs)
total_mem=$(free -h | egrep '^Mem:\s+\S+' | awk '{print $2}')
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

echo "CPU Number:" $cpu_number
echo "CPU Architecture:" $cpu_architecture
echo "CPU Model:" $cpu_model
echo "CPU Megahertz:" $cpu_mhz
echo "L2 Cache:" $l2_cache
echo "Total Memory:" $total_mem
echo "Timestamp:" $timestamp

# Usage Info
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(vmstat 1 2 | tail -1 | awk '{print $15}')
cpu_kernel=$(vmstat 1 2 | tail -1 | awk '{print $14}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -h / | awk 'NR==2 {print $4}')

echo "Free Memory:" $memory_free
echo "CPU Idle:" $cpu_idle
echo "CPU Kernel:" $cpu_kernel
echo "Disk I/O:" $disk_io
echo "Disk Available:" $disk_available