package com.itheima.bolee.framework.rule.dto;

import com.itheima.bolee.framework.rule.model.Label;

/**
 * RuleParams
 *
 * @author: wgl
 * @describe: 规则参数
 * @date: 2022/12/28 10:10
 */
public class ScoreParams {

    @Label("得分")
    private String score;


    public ScoreParams() {
    }

    public ScoreParams(String score) {
        this.score = score;
    }


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ScoreParams{" +
                "score='" + score + '\'' +
                '}';
    }
}