package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.bolee.insurance.pojo.Sick;
import com.itheima.bolee.insurance.service.DataExportService;
import com.itheima.bolee.insurance.service.ISickService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UruleDataExportServiceImpl
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Service
public class UruleDataExportServiceImpl implements DataExportService {

    @Autowired
    private ISickService sickService;

    /**
     * 数据导入
     *
     * @return
     */
    @Override
    public Object dataInport() {
        String ss = "[{\"typeName\":\"disease_01\",\"typeTitle\":\"甲状腺疾病\",\"fielldName\":\"disease_01_01\",\"fielldTitle\":\"甲状腺结节\",\"sickQuestion\":\"问卷：甲状腺结节\",\"question_a\":\"怀疑恶性活存在TI-RADS≥4级\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"针对该结果，是否有相应甲状腺结节/肿块的病理结果（包括穿刺活检）\",\"answer_b1\":\"是\",\"answer_b2\":\"否\",\"question_c\":\"病理结果是否均为良性\",\"answer_c1\":\"是\",\"answer_c2\":\"否\"},{\"typeName\":\"disease_01\",\"typeTitle\":\"甲状腺疾病\",\"fielldName\":\"disease_01_02\",\"fielldTitle\":\"甲状腺功能减退\",\"sickQuestion\":\"问卷：甲状腺功能减退\",\"question_a\":\"最近一次检查，甲状腺功能正常，未曾有过甲状腺眼病、心脏肥大、房颤、心动过速、心包积液、肢体水肿、痴呆或智力下降\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_01\",\"typeTitle\":\"甲状腺疾病\",\"fielldName\":\"disease_01_03\",\"fielldTitle\":\"甲状腺功能亢进\",\"sickQuestion\":\"问卷：甲状腺功能亢进\",\"question_a\":\"最近一次检查，甲状腺功能正常，未曾有过甲状腺眼病、心脏肥大、房颤、心动过速、心包积液、肢体水肿、痴呆或智力下降\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_02\",\"typeTitle\":\"骨科、受伤类疾病\",\"fielldName\":\"disease_02_01\",\"fielldTitle\":\"骨折\",\"sickQuestion\":\"问卷：骨折\",\"question_a\":\"是否为病理性骨折 (主要因疾病引起的骨折)，或骨折部位位于颅骨 (即头部)、脊柱 (颈椎、胸椎、腰椎) 、骨盆或伴有心、肝、肾等脏器损伤\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"（否）已基本治愈，骨折部位功能已恢复\",\"answer_b1\":\"是\",\"answer_b2\":\"否\"},{\"typeName\":\"disease_02\",\"typeTitle\":\"骨科、受伤类疾病\",\"fielldName\":\"disease_02_02\",\"fielldTitle\":\"肩周炎\",\"sickQuestion\":\"问卷：肩周炎\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_02\",\"typeTitle\":\"骨科、受伤类疾病\",\"fielldName\":\"disease_02_03\",\"fielldTitle\":\"骨质疏松\",\"sickQuestion\":\"问卷：骨质疏松\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_02\",\"typeTitle\":\"骨科、受伤类疾病\",\"fielldName\":\"disease_02_04\",\"fielldTitle\":\"腰间盘突出\",\"sickQuestion\":\"问卷：腰间盘突出\",\"question_a\":\"是否有下肢肌肉萎缩、肌力下降、尿潴留/失禁、马尾神经综合征、瘫痪\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_01\",\"fielldTitle\":\"糖尿病足\",\"sickQuestion\":\"问卷：糖尿病足\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_02\",\"fielldTitle\":\"室间隔缺损\",\"sickQuestion\":\"问卷：室间隔缺损\",\"question_a\":\"是否已手术治疗\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"已治愈满1年，不需要再进行治疗；且最近一次复查心脏超声未见异常，不存在心功能不全、心律失常或肺动脉高压\",\"answer_b1\":\"是\",\"answer_b2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_03\",\"fielldTitle\":\"心绞痛\",\"sickQuestion\":\"问卷：心绞痛\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_04\",\"fielldTitle\":\"冠心病\",\"sickQuestion\":\"问卷：冠心病\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_05\",\"fielldTitle\":\"高血压\",\"sickQuestion\":\"问卷：高血压\",\"question_a\":\"曾经或目前，无论是否服用降压药，存在收缩压≥160mmHg或舒张压≥100mmHg的情况\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_06\",\"fielldTitle\":\"糖尿病\",\"sickQuestion\":\"问卷：糖尿病\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_07\",\"fielldTitle\":\"心肌梗死\",\"sickQuestion\":\"问卷：心肌梗死\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_03\",\"typeTitle\":\"三高、心血管病\",\"fielldName\":\"disease_03_08\",\"fielldTitle\":\"先天性心脏病\",\"sickQuestion\":\"问卷：先天性心脏病\",\"question_a\":\"是否已手术治疗\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"已治愈满1年，不需要再进行治疗；且最近一次复查心脏超声未见异常，不存在心功能不全、心律失常或肺动脉高压\",\"answer_b1\":\"是\",\"answer_b2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_01\",\"fielldTitle\":\"肠胃炎\",\"sickQuestion\":\"问卷：肠胃炎\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_02\",\"fielldTitle\":\"肠道肿物或息肉\",\"sickQuestion\":\"问卷：肠道肿物或息肉\",\"question_a\":\"是否已手术切除全部息肉\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"病理结果是否均为良性\",\"answer_b1\":\"是\",\"answer_b2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_03\",\"fielldTitle\":\"胃息肉\",\"sickQuestion\":\"问卷：胃息肉\",\"question_a\":\"是否已手术切除全部息肉\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"病理结果是否均为良性\",\"answer_b1\":\"是\",\"answer_b2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_04\",\"fielldTitle\":\"胆囊息肉\",\"sickQuestion\":\"问卷：胆囊息肉\",\"question_a\":\"是否被怀疑胆囊癌\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"是否已手术切除\",\"answer_b1\":\"是\",\"answer_b2\":\"否\",\"question_c\":\"最大直径超过5cm\",\"answer_c1\":\"是\",\"answer_c2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_05\",\"fielldTitle\":\"胃肠功能紊乱\",\"sickQuestion\":\"问卷：胃肠功能紊乱\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_06\",\"fielldTitle\":\"肝血管瘤\",\"sickQuestion\":\"问卷：肝血管瘤\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_07\",\"fielldTitle\":\"十二指肠溃疡\",\"sickQuestion\":\"问卷：十二指肠溃疡\",\"question_a\":\"仅药物治疗，治疗结束距今已满1年，目前幽门螺旋杆菌阴性，且未曾有出血、穿孔、幽门狭窄或梗阻、癌变或怀疑癌变\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_08\",\"fielldTitle\":\"肝囊肿\",\"sickQuestion\":\"问卷：肝囊肿\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_09\",\"fielldTitle\":\"胆囊炎\",\"sickQuestion\":\"问卷：胆囊炎\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_10\",\"fielldTitle\":\"脂肪肝\",\"sickQuestion\":\"问卷：脂肪肝\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_11\",\"fielldTitle\":\"肝硬化\",\"sickQuestion\":\"问卷：肝硬化\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_04\",\"typeTitle\":\"食管、胃肠、肝胆胰疾病\",\"fielldName\":\"disease_04_12\",\"fielldTitle\":\"乙肝\",\"sickQuestion\":\"问卷：乙肝\",\"question_a\":\"最近一次检查，表面抗原（HBsAg）是否为阴性\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"(是）最近一次肝脏超声检查正常，或仅提示为肝囊肿、肝血管瘤、脂肪肝等\",\"answer_b1\":\"是\",\"answer_b2\":\"否\",\"question_c\":\"（否）最近一检查，e抗原（HBeAg）是否为阴性\",\"answer_c1\":\"是\",\"answer_c2\":\"否\"},{\"typeName\":\"disease_05\",\"typeTitle\":\"脑部、神经疾病\",\"fielldName\":\"disease_05_01\",\"fielldTitle\":\"脑膜炎\",\"sickQuestion\":\"问卷：脑膜炎\",\"question_a\":\"(脑膜炎) 尚未治愈或治愈不满半年，\\n或存在癫痛、瘫痪、脑积水、尿失禁、精神障碍等\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"fielldName\":\"disease_05_02\",\"fielldTitle\":\"脑炎\",\"sickQuestion\":\"问卷：脑炎\",\"question_a\":\"(脑炎) 尚未治愈或治愈不满半年，或\\n存在癫痛、瘫痪、脑积水、尿失禁、\\n精神障碍等\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"fielldName\":\"disease_05_03\",\"fielldTitle\":\"脑出血\",\"sickQuestion\":\"问卷：脑出血\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_01\",\"fielldTitle\":\"急性肺炎\",\"sickQuestion\":\"问卷：急性肺炎\",\"question_a\":\"是否已经治愈\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_02\",\"fielldTitle\":\"急性支气管炎\",\"sickQuestion\":\"问卷：急性支气管炎\",\"question_a\":\"是否已经治愈\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_03\",\"fielldTitle\":\"新型冠状病毒肺炎\",\"sickQuestion\":\"问卷：新型冠状病毒肺炎\",\"question_a\":\"新型冠状病毒肺炎，是否已经治愈\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_04\",\"fielldTitle\":\"声带息肉\",\"sickQuestion\":\"问卷：声带息肉\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_05\",\"fielldTitle\":\"肺气肿\",\"sickQuestion\":\"问卷：肺气肿\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_06\",\"fielldTitle\":\"肺结核\",\"sickQuestion\":\"问卷：肺结核\",\"question_a\":\"(肺结核) 尚未治愈或停药不满半年，\\n或有肺以外部位的结核，或为粟粒性\\n或播散性肺结核，或肺功能受损\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_07\",\"fielldTitle\":\"流感\",\"sickQuestion\":\"问卷：流感\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_08\",\"fielldTitle\":\"气胸\",\"sickQuestion\":\"问卷：气胸\",\"question_a\":\"(气胸) 存在慢性阻塞性肺病 (包括慢性支气管炎、肺气肿)、肺纤维化等疾病\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_09\",\"fielldTitle\":\"上呼吸道感染\",\"sickQuestion\":\"问卷：上呼吸道感染\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_10\",\"fielldTitle\":\"肺部肿物或结节\",\"sickQuestion\":\"问卷：肺部肿物或结节\",\"question_a\":\"是否已手术治疗\",\"answer_a1\":\"是\",\"answer_a2\":\"否\",\"question_b\":\"有全部病灶的病理结果，均为良性\",\"answer_b1\":\"是\",\"answer_b2\":\"否\",\"question_c\":\"目前是否已经手术彻底切除\",\"answer_c1\":\"是\",\"answer_c2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_11\",\"fielldTitle\":\"支气管肺炎\",\"sickQuestion\":\"问卷：支气管肺炎\",\"question_a\":\"(支气管炎) 尚未治愈，或被诊断为\\n慢性支气管炎\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_12\",\"fielldTitle\":\"哮喘\",\"sickQuestion\":\"问卷：哮喘\",\"question_a\":\"(哮喘) 目前是否吸烟，或曾经及目前\\n患有慢性支气管炎、肺气肿、慢性阻\\n塞性肺病肺源性心脏病或心功能不全\\n等疾病\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_06\",\"typeTitle\":\"呼吸系统疾病\",\"fielldName\":\"disease_06_13\",\"fielldTitle\":\"支气管扩张\",\"sickQuestion\":\"问卷：支气管扩张\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_07\",\"typeTitle\":\"肿瘤\",\"fielldName\":\"disease_07_01\",\"fielldTitle\":\"胰腺癌\",\"sickQuestion\":\"问卷：胰腺癌\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_07\",\"typeTitle\":\"肿瘤\",\"fielldName\":\"disease_07_02\",\"fielldTitle\":\"肺癌\",\"sickQuestion\":\"问卷：肺癌\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_07\",\"typeTitle\":\"肿瘤\",\"fielldName\":\"disease_07_03\",\"fielldTitle\":\"肾癌\",\"sickQuestion\":\"问卷：肾癌\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_07\",\"typeTitle\":\"肿瘤\",\"fielldName\":\"disease_07_04\",\"fielldTitle\":\"食管癌\",\"sickQuestion\":\"问卷：食管癌\",\"question_a\":\"请确认以上问题选项无误并已如实回答\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"},{\"typeName\":\"disease_07\",\"typeTitle\":\"肿瘤\",\"fielldName\":\"disease_07_05\",\"fielldTitle\":\"甲状腺癌\",\"sickQuestion\":\"问卷：甲状腺癌\",\"question_a\":\"(甲状腺癌) 是否同时满足以下两个条件:\\nD有病理结果，并且结果仅为乳,头状癌\\n或滤泡状癌;2没有远处转移 (说明: 远\\n处转移是指肿瘤TNM分期中M分期为M1\\n的情况\",\"answer_a1\":\"是\",\"answer_a2\":\"否\"}]";
        List<SickData> list = JSONUtil.toList(ss, SickData.class);
        System.out.println(list);
        System.out.println(list.size());
        ArrayList<Sick> sicks = new ArrayList<>();
        list.stream().forEach(e->{
            Sick sick = new Sick();
            String typeName = e.getTypeName();
            sick.setSickType(typeName);
            String fielldName = e.getFielldName();
            sick.setSickKey(fielldName);
            sick.setSickKeyName(e.getFielldTitle());
            Template template = new Template();
            template.setQuestionnaireName(e.getSickQuestion());
            List<Question> questions = new ArrayList<>();
            if(ObjectUtil.isNotNull(e.getQuestion_a())){
                Question question = new Question();
                question.setQuestionKey(fielldName+"_01");
                question.setQuestion(e.getQuestion_a().replaceAll("\r",""));
                question.setQuestion(question.getQuestion().replaceAll("\n",""));
                question.setAnswer(Arrays.asList(true,false));
                questions.add(question);
            }

            if(ObjectUtil.isNotNull(e.getQuestion_b())){
                Question question = new Question();
                question.setQuestion(e.getQuestion_b().replaceAll("\r",""));
                question.setQuestion(question.getQuestion().replaceAll("\n",""));
                question.setQuestionKey(fielldName+"_02");
                question.setAnswer(Arrays.asList(true,false));
                questions.add(question);
            }
            if(ObjectUtil.isNotNull(e.getQuestion_c())){
                Question question = new Question();
                question.setQuestion(e.getQuestion_c().replaceAll("\r",""));
                question.setQuestion(question.getQuestion().replaceAll("\n",""));
                question.setQuestionKey(fielldName+"_03");
                question.setAnswer(Arrays.asList(true,false));
                questions.add(question);
            }
            template.setQuestion(questions);
            sick.setQuestion(JSONUtil.toJsonStr(template));
            sick.setSickVal(e.getFielldName());
            sicks.add(sick);
        });
        System.out.println(sicks);
        sickService.saveBatch(sicks);
        return null;
    }

    /**
     * SickData
     *
     * @author: wgl
     * @describe: TODO
     * @date: 2022/12/28 10:10
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public class SickData {

        private String typeName;

        private String typeTitle;

        private String fielldName;

        private String fielldTitle;

        private String sickQuestion;

        private String question_a;

        private String answer_a1;

        private String answer_a2;

        private String question_b;

        private String answer_b1;

        private String answer_b2;

        private String question_c;

        private String answer_c1;

        private String answer_c2;
    }

    @Data
    class Question {

        private String question;

        private String questionKey;

        private List<Boolean> answer;

    }

    @Data
    class Template {
        //问卷名称
        private String questionnaireName;

        private List<Question> question;
    }

    /**
     * 数据修改
     * @return
     */
    @Override
    public Object dataUpdate() {
        LambdaQueryWrapper<Sick> query = new LambdaQueryWrapper<>();
        query.like(Sick::getQuestion, "请确认以上问题选项无误并已如实回答");
        List<Sick> list = sickService.list(query);
        System.out.println(list);
        for (Sick index: list) {
            String question = index.getQuestion();
            Template template = JSONUtil.toBean(question, Template.class);
            for (Question indexQuestion : template.getQuestion()) {
                if(indexQuestion.getQuestion().equals("请确认以上问题选项无误并已如实回答")){
                    indexQuestion.setQuestionKey("disease_00_00_01");
                }
            }
            JSON parse = JSONUtil.parse(template);
            index.setQuestion(parse.toString());
        }
        sickService.updateBatchById(list);
        return null;
    }
}
