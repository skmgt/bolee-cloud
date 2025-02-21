package com.itheima.bolee.insurance.urule.action;

import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionBean;
import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethod;
import com.itheima.bolee.framework.rule.model.library.action.annotation.ActionMethodParameter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RiskScore
 *
 * @author: wgl
 * @describe: 风险打分
 * @date: 2022/12/28 10:10
 */
@ActionBean(name = "打分")
@Component
@Slf4j
public class RiskScore {

    @Autowired
    RedissonClient redissonClient;

    @ActionMethod(name = "风险打分")
    @ActionMethodParameter(names = {"用户id","风险名称", "得分","风险类型"})
    public void setScore(String userId,String riskName, String score,String riskType) {
        // 创建不同用户的ZSet键
        String userZSetKey = "user_risk_scores:" +riskType+":"+ userId;
        // 获取RScoredSortedSet对象，它代表了ZSet
        RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(userZSetKey);
        // 获取当前用户的风险分数
        Double currentScore = zset.getScore(riskName);
        if (currentScore == null || Double.parseDouble(score) > currentScore) {
            // 如果分数不存在或新分数更高，更新分数
            zset.add(Double.parseDouble(score), riskName);
            System.out.println("分数更新成功");
        } else {
            System.out.println("分数未更新");
        }
    }

    @ActionMethod(name = "追加打分")
    @ActionMethodParameter(names = {"用户id","风险名称", "得分","风险类型"})
    public void appendScore(String userId,String riskName, String score,String riskType) {
        try {
            // 创建不同用户的ZSet键
            String userZSetKey = "user_risk_scores:" + riskType + ":" + userId;
            // 获取RScoredSortedSet对象，它代表了ZSet
            RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(userZSetKey);
            // 获取当前用户的风险分数
            Double currentScore = zset.getScore(riskName);
            // 如果分数不存在或新分数更高，更新分数
            zset.add(Double.parseDouble(score) + currentScore, riskName);
            System.out.println("分数更新成功");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}