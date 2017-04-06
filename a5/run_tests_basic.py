#!/usr/bin/python

import sys
import subprocess
import re

# String to determine if semantic error
semantic_errorname = "SemanticErrorException"
codegen_errorname = "CodeGenErrorException"

def print_parse_error():
    print "Error parsing parameters.\n Usage: %s <passing | failing>" % sys.argv[0]

def get_error_msg(output, exception_name):
    try:
        msg_start = output.find(exception_name) + len(exception_name)
        msg_end = output.find("at compiler488.ast", msg_start + 1)
        return output[msg_start:msg_end]
    except:
        return ""

def get_output(output):
    end = max(output.find('End Execution.'), output.find('Execution Error'))
    exec_start = output.find('Start Execution')
    start = output.find('\n', exec_start)

    return output[start + 3:end - 1] # remove extra tabs/newlines

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
    files = set(files)

    fail_count = 0
    num_test_cases = len(files)
    
    print "-----------------------------------------"
    print "Running " + str(num_test_cases) + " tests"
    print "-----------------------------------------"
    
    for file in files:
        # don't auto run tests which need input
        if file.endswith('.488') and 'read' not in file and sys.argv[1] == 'passing':
            print "Testing file: " + file
            test_case_path = test_cases_path + file
            compile_args = ["sh", "RUNCOMPILER.sh", test_case_path]
            compile_out = subprocess.check_output(compile_args, stderr=subprocess.STDOUT)
            if "Syntax error" in compile_out:
                print '\tSyntax error'
            # elif (semantic_errorname in compile_out):
            #     print '\tSemantic error' + get_error_msg(compile_out, semantic_errorname)
            #     fail_count += 1
            elif (codegen_errorname in compile_out):
                print '\tCodegen error' + get_error_msg(compile_out, codegen_errorname)
            elif ".Exception" in compile_out:
                print ''
                print compile_out
                print '-----------------------------------------'
            else: # ran without errors, check for correct output
                expected_file = file.split('.')[0] + '.expected'
                expected_file_path = test_cases_path + expected_file
                if expected_file in files:
                    expected = open(expected_file_path, 'r').read()
                    try:
                        got = get_output(compile_out)
                    except:
                        print '\tCould not get program output\n'
                    else:
                        if expected == got:
                            print '\tOutput matches expected\n'
                        else:
                            fail_count += 1
                            print '\tExpected:'
                            print expected
                            print '\tBut got:'
                            print got
                            print
                else:
                    print '\tCould not find expected output file\n'
        
    print "-----------------------------------------"        
    print str(fail_count) + " test cases failed out of " + str(num_test_cases)
    print "-----------------------------------------"
