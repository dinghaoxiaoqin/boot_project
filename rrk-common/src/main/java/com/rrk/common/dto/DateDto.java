package com.rrk.common.dto;

import com.rrk.common.handle.MyException;
import com.rrk.common.utils.DateUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * 开始时间和结束时间的处理
 */
@Data
public class DateDto implements Serializable {


    private final String REG = "[\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}";



    /**
     * 开始时间
     */
    private Date start;
    /**
     * 结束时间
     */
    private Date end;

    public DateDto() {
    }

    public DateDto(String startTime, String endTime) throws ParseException {
        if (StringUtils.isNotBlank(startTime)) {
           if (!startTime.matches(REG)) {throw new MyException("时间传入格式不正确："+startTime+"；按：yyyy-MM-dd HH:mm:ss格式");}
            start = DateUtils.parseDate(startTime, DateUtils.DATE_TIME_PATTERN);
        }
        if (StringUtils.isNotBlank(endTime)) {
           if (!endTime.matches(REG)) {throw new MyException("时间传入格式不正确："+endTime+"；按：yyyy-MM-dd HH:mm:ss格式");}
            end = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss");
        }
        this.setStart(start);
        this.setEnd(end);
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DateDto{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public void setEnd(Date end) {
        this.end = end;

    }

//    public static void main(String[] args) {
//        String str = "2019-10-31";
//
//        boolean matches = str.matches("[\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}");
//        System.out.println(matches);
//    }
}
