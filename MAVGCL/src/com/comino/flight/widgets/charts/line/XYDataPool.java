/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/


package com.comino.flight.widgets.charts.line;

import java.util.Enumeration;
import java.util.Hashtable;

import javafx.scene.chart.XYChart;

public class XYDataPool {

	private Hashtable<XYChart.Data<Number,Number>,Boolean> locked, unlocked;

	public XYDataPool() {
		locked = new Hashtable<XYChart.Data<Number,Number>,Boolean>();
		unlocked = new Hashtable<XYChart.Data<Number,Number>,Boolean>();
	}

	public synchronized XYChart.Data<Number,Number> checkOut(float x, float y)
	{
		XYChart.Data<Number,Number> o;
		if( unlocked.size() > 0 )
		{
			Enumeration<XYChart.Data<Number,Number>> e = unlocked.keys();
			o = e.nextElement();
			o.setXValue(x);
			o.setYValue(y);
			unlocked.remove(o);
			locked.put(o, true);
			return(o);
		}
		o = new XYChart.Data<Number,Number>(x,y);
		locked.put( o, true );
		return( o );
	}

	public synchronized void invalidate(XYChart.Data<Number,Number> o) {
		if(locked.size()>0) {
			locked.remove(o);
			unlocked.put(o, true);
		}
	}

	public synchronized void invalidateAll() {
		unlocked.clear();
		locked.clear();
	}

	public int getLockedSize() {
		return locked.size();
	}

}
