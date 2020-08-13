/*
 * FilterExample.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.waikatoufdl.ufdl4j.examples;

import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.filter.AbstractExpression;
import com.github.waikatoufdl.ufdl4j.filter.Filter;
import com.github.waikatoufdl.ufdl4j.filter.OrderBy;
import com.github.waikatoufdl.ufdl4j.filter.field.Contains;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactNumber;
import com.github.waikatoufdl.ufdl4j.filter.field.ExactString;
import com.github.waikatoufdl.ufdl4j.filter.logical.And;
import com.github.waikatoufdl.ufdl4j.filter.logical.Or;

/**
 * Example code for using filters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FilterExample {

  /**
   * Expects three arguments:
   * 1. backend URL, eg http://127.0.0.1:8000
   * 2. backend user, eg admin
   * 3. user password, eg admin
   * Otherwise the above default values are used
   *
   * @param args	the parameters to supply
   * @throws Exception	if queries fail for some reason
   */
  public static void main(String[] args) throws Exception {
    System.out.println("--> connecting to backend");
    Client client;
    if (args.length < 3)
      client = new Client("http://127.0.0.1:8000", "admin", "admin");
    else
      client = new Client(args[0], args[1], args[1]);

    // general filter setup
    Filter filter = new Filter(
      new AbstractExpression[]{
	new Or(
	  new AbstractExpression[]{
	    new And(
	      new AbstractExpression[]{
		new ExactString("name", "robert", true),
		new ExactNumber("height", 1.75),
	      }
	    ),
	    new And(
	      new AbstractExpression[]{
		new ExactString("name", "robert", true, true),
		new Contains("name", "rob", true),
		new ExactNumber("height", 2.0, true),
	      }
	    ),
	  }
	),
      },
      new OrderBy[]{
	new OrderBy("height", false),
	new OrderBy("name"),
	new OrderBy("other", true, true),
      },
      false);
    System.out.println("--> general filter setup");
    System.out.println(filter.toPrettyPrint());
  }
}
