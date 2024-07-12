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

# Save hostname, hardware info, and machine statistics in MB to a variable
hostname=$(hostname -f)
lscpu_out=`lscpu`
vmstat_mb=$(vmstat --unit M)

# Hardware specifications
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{ $1=$2=""; print substr($0,3) }' | xargs)
cpu_mhz=$(grep "cpu MHz" /proc/cpuinfo | head -1 | awk '{print $4}'| xargs)
l2_cache=$(echo "$lscpu_out" | egrep "L2 cache" | awk '{print $3}' | xargs)
total_mem=$(free -m | egrep '^Mem:\s+\S+' | awk '{print $2}' | xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Insert data into host_info table on PSQL
insert_stmt="INSERT INTO host_info(hostname, cpu_number,cpu_architecture,cpu_model,cpu_mhz,l2_cache,\"timestamp\",\
total_mem) VALUES('$hostname', $cpu_number,'$cpu_architecture','$cpu_model',$cpu_mhz,$l2_cache,'$timestamp',$total_mem)"

# Environment variable for psql command
export PGPASSWORD=$psql_password

# Insert data into Postgresql database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit #?
