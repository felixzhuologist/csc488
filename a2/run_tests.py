#!/usr/bin/python

import sys
import subprocess
import re

def print_parse_error():
    print "Error parsing parameters.\n Usage: %s <passing | failing>" % sys.argv[0]

if __name__ == "__main__":

    if len(sys.argv) != 2:
        print_parse_error()
        sys.exit()

    test_case_path = "";
    if sys.argv[1] == "passing":
        test_cases_path = "./tests/passing/"
    elif (sys.argv[1] == "failing"):
        test_cases_path = "./tests/failing/"
    else:
        print_parse_error()
        sys.exit()

    # Compile
    ant_args = ["ant", "dist"]
    subprocess.check_output(ant_args, stderr=subprocess.STDOUT)

    # Locate test cases
    ls_args = ["ls", test_cases_path]
    ls_out = subprocess.check_output(ls_args, stderr=subprocess.STDOUT)

    files = re.split(r'\n+', ls_out)
    files.pop(-1)

    # String to determine if case passes
    pass_string = "Start parsing\nEnd parsing\n"
    pass_count = 0
    num_test_cases = len(files)
    
    print "-----------------------------------------"
    print "Running " + str(num_test_cases) + " tests"
    print "-----------------------------------------"
    
    # Run compile on all test cases
    for file in files:
        print "Testing file: " + file
        test_case_path = test_cases_path + "/" + file
        compile_args = ["sh", "RUNCOMPILER.SH", test_case_path]
        compile_out = subprocess.check_output(compile_args, stderr=subprocess.STDOUT)
        if (compile_out == pass_string):
            pass_count += 1
        print(compile_out)
        
    print "-----------------------------------------"        
    print str(pass_count) + " test cases passed out of " + str(num_test_cases)
    print "-----------------------------------------"
