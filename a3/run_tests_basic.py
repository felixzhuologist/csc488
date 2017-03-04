#!/usr/bin/python

import sys
import subprocess
import re

# String to determine if semantic error
semantic_error_string = "SemanticErrorException"

def print_parse_error():
    print "Error parsing parameters.\n Usage: %s <passing | failing>" % sys.argv[0]

def get_semantic_error_msg(output):
    try:
        msg_start = output.find(semantic_error_string) + len(semantic_error_string)
        msg_end = output.find("at compiler488.ast", msg_start + 1)
        return output[msg_start:msg_end]
    except:
        return ""

if __name__ == "__main__":

    if len(sys.argv) != 2:
        print_parse_error()
        sys.exit()

    test_case_path = ""
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

    fail_count = 0
    num_test_cases = len(files)
    
    print "-----------------------------------------"
    print "Running " + str(num_test_cases) + " tests"
    print "-----------------------------------------"
    
    # Run compile on all test cases
    for file in files:
        print "Testing file: " + file
        test_case_path = test_cases_path + file
        compile_args = ["sh", "RUNCOMPILER.sh", test_case_path]
        compile_out = subprocess.check_output(compile_args, stderr=subprocess.STDOUT)
        if "Syntax error" in compile_out:
            print '\tSyntax error'
        elif (semantic_error_string in compile_out):
            print '\tSemantic error' + get_semantic_error_msg(compile_out)
            fail_count += 1
        elif "Exception" in compile_out:
            print '\tOther error'
        
    print "-----------------------------------------"        
    print str(fail_count) + " test cases failed out of " + str(num_test_cases)
    print "-----------------------------------------"
