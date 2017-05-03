package com.market.aching.util;

import java.util.Iterator;
import java.util.Stack;

import android.app.Activity;

/**
 * activity管理类
 * */
public class ScreenManager
{
	private static final String TAG = ScreenManager.class.getSimpleName();
	private static Stack<Activity>	activityStack;
	private static ScreenManager	instance;

	private ScreenManager()
	{

	}

	public static ScreenManager getScreenManager()
	{
		if (instance == null)
		{
			synchronized (ScreenManager.class)
			{
				if (null == instance)
				{
					instance = new ScreenManager();
				}
			}
		}
		return instance;
	}

	/**
	 * 弹出栈并finish activity
	 */
	public void popActivityFinish()
	{
		Activity activity = null;
		if (activityStack != null && !activityStack.isEmpty())
		{
			activity = activityStack.pop();
			if (activity != null)
			{
				activity.finish();
				activity = null;
			}
		}
	}
	
	/**
	 * 如果栈顶为cls，则弹出
	 * @param cls
	 */
	public void popTopActivityEquals(Class<?> cls)
	{
		if (activityStack != null && !activityStack.isEmpty())
		{
			if (activityStack.lastElement().getClass().equals(cls))
			{
				activityStack.pop();
			}
		}
	}

	/**
	 * 只弹出栈但不finish
	 */
	public void popActivityNoFinish()
	{
		if (activityStack != null)
		{
			int size = getSize();
			if (size > 0)
				activityStack.pop();
		}
	}

	/**
	 * 清除并finish Activity
	 */
	public void clearAllActivity()
	{
		if (activityStack != null)
		{
			int size = getSize();
			boolean isEmpty = activityStack.isEmpty();
			while (size > 0 && !isEmpty)
			{
				popActivityFinish();
				isEmpty = activityStack.isEmpty();
				size--;
			}
			activityStack.clear();
		}
	}

	/**
	 * 清除栈中的内容，但并不finish
	 */
	public void clearAllNoFinish()
	{
		if (activityStack != null)
		{
			int size = getSize();
			boolean isEmpty = activityStack.isEmpty();
			while (size > 0 && !isEmpty)
			{
				popActivityNoFinish();
				isEmpty = activityStack.isEmpty();
				size--;
			}
			activityStack.clear();
		}
	}

	/**
	 * 入栈
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity)
	{
		if (activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
	}

	/**
	 * 取得栈顶的activity
	 * 
	 * @return
	 */
	public Activity getTopActivity()
	{
		Activity activity = null;
		if (activityStack != null && !activityStack.isEmpty())
			activity = activityStack.lastElement();
		return activity;
	}
	
	/**
	 * 取得栈底的activity
	 * @return
	 */
	public Activity getBottomActivity(){
		Activity activity = null;
		if (activityStack != null && !activityStack.isEmpty())
			activity = activityStack.firstElement();
		return activity;
	}

	/**
	 * 遍历stack
	 */
	public void listStack(){
		if(activityStack != null)
		{
			Iterator<Activity> it = activityStack.iterator();
			while (it.hasNext())
			{
				Activity activity = it.next();
				String classname = activity.getComponentName().getClassName();
			}
		}
	}
	
	/**
	 * 弹出直到是cls类型的，并finish
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class<?> cls)
	{
		if (activityStack != null && !activityStack.isEmpty())
			while (true)
			{
				Activity activity = activityStack.pop();
				if (activity == null)
				{
					break;
				}
				if (activity.getClass().equals(cls))
				{
					break;
				}
				activity.finish();
				activity = null;
			}
	}

	/**
	 * 弹出直到是cls类型的,但并不finish
	 * 
	 * @param cls
	 */
	public void popAllNoFinishExceptOne(Class<?> cls)
	{
		while (true)
		{
			Activity activity = getTopActivity();
			if (activity == null)
			{
				break;
			}
			if (activity.getClass().equals(cls))
			{
				break;
			}
		}
	}
	
	/**
	 * 弹出直到指定类
	 * @param cls
	 */
	public void popActivityUntil(Class<?> cls)
	{
		while (true)
		{
			if (activityStack == null || activityStack.isEmpty())
			{
				break;
			}
			Activity activity = activityStack.pop();
			if (activity == null)
			{
				break;
			}
			if (activity.getClass().equals(cls))
			{
				activityStack.push(activity);
				break;
			}
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * 栈的大小
	 * 
	 * @return
	 */
	public int getSize()
	{
		int size = 0;
		if (activityStack != null)
		{
			size = activityStack.size();
		}
		return size;
	}

	/**
	 * 获得第几个元素
	 * 
	 * @param i
	 * @return
	 */
	public Activity getElement(int i)
	{
		Activity activity = null;
		if (activityStack != null)
			activity = activityStack.elementAt(i);
		return activity;
	}
	
	/**
	 * 栈是否为空
	 * @return
	 */
	public boolean isEmpty(){
		boolean isEmpty = true;
		if(activityStack!=null){
			isEmpty  = activityStack.isEmpty();
		}
		return isEmpty;
	}

}
