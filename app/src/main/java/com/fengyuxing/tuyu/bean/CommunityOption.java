package com.fengyuxing.tuyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 社区发布动态获取预加载数据，如变化幅度等
 * @author LGJ
 */
public class CommunityOption implements Serializable {

	private static final long serialVersionUID = 2437848784778095851L;

	private List<Option> changeTime;//变化时间
	private List<Option> changeTrend;//变化趋势
	private List<Option> increase; //涨幅
	private List<Option>	jcskzlist;

	public List<Option> getJcskzlist() {
		return jcskzlist;
	}

	public void setJcskzlist(List<Option> jcskzlist) {
		this.jcskzlist = jcskzlist;
	}

	public static class Option  {

		private String name;//
		private boolean checked;


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}


	public List<Option> getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(List<Option> changeTime) {
		this.changeTime = changeTime;
	}

	public List<Option> getChangeTrend() {
		return changeTrend;
	}

	public void setChangeTrend(List<Option> changeTrend) {
		this.changeTrend = changeTrend;
	}

	public List<Option> getIncrease() {
		return increase;
	}

	public void setIncrease(List<Option> increase) {
		this.increase = increase;
	}
}
