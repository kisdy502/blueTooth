package com.kisdy.sdt13411.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 摸你测试数据
 * Created by sdt13411 on 2015/12/3.
 */
public class FestivalLab {
    private static FestivalLab mInstance;
    private List<FestivalBean> beanList =new ArrayList<FestivalBean>();

    private  List<Msg> msgList=new ArrayList<Msg>();

    private  FestivalLab() {
        beanList.add(new FestivalBean(1,"春节"));
        beanList.add(new FestivalBean(2,"元宵节"));
        beanList.add(new FestivalBean(3,"清明节"));
        beanList.add(new FestivalBean(4,"劳动节"));
        beanList.add(new FestivalBean(5,"儿童节"));
        beanList.add(new FestivalBean(6,"青年节"));
        beanList.add(new FestivalBean(7,"中秋节"));
        beanList.add(new FestivalBean(8,"国庆节"));
        beanList.add(new FestivalBean(9,"元旦"));
        beanList.add(new FestivalBean(10,"除夕"));
        beanList.add(new FestivalBean(11,"妇女节"));
        beanList.add(new FestivalBean(12,"光棍节"));
        beanList.add(new FestivalBean(13,"情人节"));



        msgList.add(new Msg(31,1,"春节快乐1"));
        msgList.add(new Msg(32,1,"春节快乐2"));

        msgList.add(new Msg(29,2,"元宵节快乐1"));
        msgList.add(new Msg(30,2,"元宵节快乐2"));

        msgList.add(new Msg(27,3,"清明节快乐1"));
        msgList.add(new Msg(28,3,"清明节快乐2"));

        msgList.add(new Msg(25,4,"劳动节快乐1"));
        msgList.add(new Msg(26,4,"劳动节快乐2"));

        msgList.add(new Msg(23,5,"儿童节快乐1"));
        msgList.add(new Msg(24,5,"儿童节快乐2"));

        msgList.add(new Msg(21,6,"青年节快乐1"));
        msgList.add(new Msg(22,6,"青年节快乐2"));

        msgList.add(new Msg(19,7,"中秋节快乐1"));
        msgList.add(new Msg(20,7,"中秋节快乐2"));
        msgList.add(new Msg(8,7,"海上明月共潮生，千里相思随云去，遥寄祝福千万缕，化作清风入梦里。中秋快乐!"));
        msgList.add(new Msg(9,7,"明月本无价，高山皆有情。人虽不至，心向往之。衷心祝愿您和家人团圆美满，幸福安康!"));
        msgList.add(new Msg(10,7,"愿中秋佳节：家人关心你、爱情滋润你、财神宠幸你、朋友忠于你、我会祝福你、幸运之星永远照着你"));

        msgList.add(new Msg(19,8,"国庆节快乐1"));
        msgList.add(new Msg(20,8,"国庆节快乐2"));


        msgList.add(new Msg(17,9,"我想要昙花永不凋谢，开在温暖春天!我想要冬天阳光灿烂，化开寒冷冰雪!我更想要看短信的人在新年开心快乐，健康幸福一整年!"));
        msgList.add(new Msg(18,9,"为答谢朋友多年来关心支持，本人特在元旦期间大酬宾--凡在我心中有地位的人都将获得由我提供的价值人民币一毛的元旦祝福短信一条!别忘请我吃饭哦!"));

        msgList.add(new Msg(15,10,"除夕快乐1"));
        msgList.add(new Msg(16,10,"除夕快乐2"));

        msgList.add(new Msg(13,11,"妇女节快乐1"));
        msgList.add(new Msg(14,11,"妇女节快乐2"));

        msgList.add(new Msg(11,12,"光棍节快乐1"));
        msgList.add(new Msg(12,12,"光棍节快乐2"));



        //情人节短信数据
        msgList.add(new Msg(1,13,"看着满街的玫瑰我想起了你；含着微苦的巧克力我想起了你；翻开署名不是你的情人卡，我想起了你；很想轻轻的问你，这个情人节，过得还好吗？"));
        msgList.add(new Msg(2,13,"请相信缘分，上天很快就会赐给你一个完美的情人。那么今天，先让我充当你的情人，温暖你孤单的心，情人节快乐！"));
        msgList.add(new Msg(3,13,"情人节快乐！如果你要过一个孤独的情人节，不要难过！那个能给你带来快乐的人很快会出现在你身边，相信我！"));
        msgList.add(new Msg(4,13,"初次的相见，你便是我无法抹去的思念；命运捉弄，你我擦肩而过。纵使有缘无份，我依然是你忠实的朋友，衷心祝福你情人节快乐"));
        msgList.add(new Msg(5,13,"给你点阳光你就灿烂，给你点洪水你就泛滥。破锅自有破锅盖，丑鬼自有丑女爱，只要情深意似海，麻子也能放光彩！"));
        msgList.add(new Msg(6,13,"你曾说我爱上你，就像刺猬爱上玫瑰，为了不彼此伤害，我选择了离开。情人节了，刺猬在远方为玫瑰祝福：早日找到小王子，幸福快乐每一天。"));
    }



    public static FestivalLab getInstance() {
        if (mInstance == null) {
            synchronized (FestivalLab.class){
                if (mInstance == null) {
                    mInstance =new FestivalLab();
                }
            }
        }
        return mInstance;
    }

    public List<FestivalBean> getFestivalList(){
        return new ArrayList<FestivalBean>(beanList);
    }

    public FestivalBean getFestivalByFestId(int id){
        for (FestivalBean bean:beanList){
            if(bean.getId()==id)
                return bean;
        }
        return null;
    }

    public List<Msg> getMsgsByFestivalId(int FestivalId){
        List<Msg> resultList=new ArrayList<Msg>();
        for(Msg msg:msgList){
            if(msg.getFestivalId()==FestivalId)
                resultList.add(msg);
        }
        return resultList;
    }
    public Msg getMsgByMsgId(int msgId){
        for(Msg msg:msgList){
            if(msg.getMsgId()==msgId)
                return msg;
        }
        return null;
    }
}
