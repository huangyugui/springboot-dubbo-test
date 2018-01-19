package com.xu.drools.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 报警消息格式
 *
 * @author Jann Liu
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Message implements Serializable {
    private static final long serialVersionUID = 6369798298924000511L;

    /**
     * 版本
     */
    private String version;
    /**
     * Datetime: long : {@link System#currentTimeMillis()}
     */
    private String timestamp;

    /***************************告警坐标***************************/
    /**
     * 业务线ID
     */
    private String businessGroupId;
    /**
     * 报警系统的ID
     */
    private String systemId;
    /**
     * IP 地址
     */
    private String ip;
    /**
     * 部署实例进程号
     */
    private String deployProcessId;


    /***************************告警内容***************************/
    /**
     * 每一条报警消息一个唯一ID
     */
    private String alarmId;
    /**
     * 告警级别
     * @see AlarmLevel
     */
    private AlarmLevel alarmLevel;
    /**
     * 告警类型
     * @see AlarmType
     */
    private AlarmType alarmType;
    /**
     * 业务类型，由各个业务方来定义，主要用于精准的匹配策略
     */
    private String businessType;
    /**
     * 策略ID，选择用特定的策略进行发送告警，如果多个可以使用“,”隔开
     */
    private String policy;
    /**
     * 是否只是用指定的策略
     */
    private boolean policyOnly = false;
    /**
     * 告警名称
     */
    private String alarmName;
    /**
     * 告警信息摘要
     */
    private String alarmDigest;
    /**
     * 参变量模板ID
     */
    private String templateId;
    /**
     * 报警内容，填充模板的参数
     */
    private String contentJson;

    private boolean hitted = false;

}
