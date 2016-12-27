package com.myframe.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.myframe.db.DatabaseHelper;
import com.myframe.db.entity.TomatoTask;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Aaron on 2016/7/6.
 */
public class TomatoTaskDao {
    private Dao<TomatoTask,Integer> tomatoTaskDaoOpe;

    public TomatoTaskDao(Context context) {
        try {
            tomatoTaskDaoOpe = DatabaseHelper.getHelper(context).getDao(TomatoTask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加Task
     * @param task
     */
    public boolean addTask(TomatoTask task){
        /*//事务操作
        TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
        try {
            if(tomatoTaskDaoOpe.create(task)!= 1){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过ID查找Task
     * @param id
     * @return
     */
    public TomatoTask getTaskById(int id){
        TomatoTask tomatoTask = null;
        try {
            tomatoTask = tomatoTaskDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tomatoTask;
    }

    /**
     * 查找没有完成的任务
     * @return
     */
    public List<TomatoTask> getAllTodoTask(){
        try {
            return tomatoTaskDaoOpe.queryBuilder().where().eq("isFinished",false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Id删除
     * @param id
     * @return
     */
    public boolean delTaskById(int id){
        try {
            if(tomatoTaskDaoOpe.deleteById(id)!=1){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 刷新任务
     * @param task
     * @return
     */
    public boolean updataTask(TomatoTask task){
        try {
            if(tomatoTaskDaoOpe.update(task)!=1){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查找完成的任务
     * @return
     */
    public List<TomatoTask> getAllFinishedTask(){
        try {
            return tomatoTaskDaoOpe.queryBuilder().orderBy("end",true).where().eq("isFinished",true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
