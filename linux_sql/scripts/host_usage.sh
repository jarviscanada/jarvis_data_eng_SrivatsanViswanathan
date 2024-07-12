#!/bin/bash

#  CLI arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check number of args
if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

# Save hostname and machine statistics in MB to a variable
hostname=$(hostname -f)
vmstat_mb=$(vmstat --unit M)

# Hardware specifications
memory_free=$(echo "$vmstat_mb" | tail -1 |  awk '{print $4}'| xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(vmstat -d| tail -1 | awk '{print $10}' | xargs)
disk_available=$(df -BM / | awk 'NR==2 {print $4}' | tr -d 'M' | xargs)

# Timestamp
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Query to find matching id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

# Insert data into host_usage table on PSQL
insert_stmt="INSERT INTO host_usage(\"timestamp\",host_id,memory_free,cpu_idle,cpu_kernel,disk_io,disk_available) \
VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available)"

# Environment variable for psql command
export PGPASSWORD=$psql_password

# Insert data into Postgresql database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
