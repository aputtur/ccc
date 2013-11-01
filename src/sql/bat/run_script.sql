define file_to_run=&1
define credentials=&2
define arg1=&3
define arg2=&4
define arg3=&5

@.\connect.sql &credentials

@&file_to_run &arg1 &arg2 &arg3

exit;

