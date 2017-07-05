/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

/**
* @test
* @summary Add C2 x86 Superword support for scalar sum reduction optimizations : double sqrt test
*
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:+SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=2 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:-SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=2 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
*
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:+SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=4 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:-SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=4 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
*
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:+SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=8 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:-SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=8 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
*
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:+SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=16 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
* @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:-SuperWordReductions -XX:LoopUnrollLimit=250 -XX:LoopMaxUnroll=16 -XX:CompileThresholdScaling=0.1 SumRedSqrt_Double
*/

public class SumRedSqrt_Double
{
  public static void main(String[] args) throws Exception {
    double[] a = new double[256*1024];
    double[] b = new double[256*1024];
    double[] c = new double[256*1024];
    double[] d = new double[256*1024];
    sumReductionInit(a,b,c);
    double total = 0;
    double valid = 2.06157643776E14;
    for(int j = 0; j < 2000; j++) {
      total = sumReductionImplement(a,b,c,d,total);
    }
    if(total == valid) {
      System.out.println("Success");
    } else {
      System.out.println("Invalid sum of elements variable in total: " + total);
      System.out.println("Expected value = " + valid);
      throw new Exception("Failed");
    }
  }

  public static void sumReductionInit(
    double[] a,
    double[] b,
    double[] c)
  {
    for(int j = 0; j < 1; j++)
    {
      for(int i = 0; i < a.length; i++)
      {
        a[i] = i * 1 + j;
        b[i] = i * 1 - j;
        c[i] = i + j;
      }
    }
  }

  public static double sumReductionImplement(
    double[] a,
    double[] b,
    double[] c,
    double[] d,
    double total)
  {
    for(int i = 0; i < a.length; i++)
    {
      d[i]= Math.sqrt(a[i] * b[i]) + Math.sqrt(a[i] * c[i]) + Math.sqrt(b[i] * c[i]);
      total += d[i];
    }
    return total;
  }

}
