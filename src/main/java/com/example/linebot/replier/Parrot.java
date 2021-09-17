package com.example.linebot.replier;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import java.sql.*;
import java.time.LocalDate;

// おうむ返し用の返信クラス
public class Parrot implements Replier {

    private MessageEvent<TextMessageContent> event;

    public Parrot(MessageEvent<TextMessageContent> event) {
        this.event = event;
    }

    @Override
    public Message reply() {
        TextMessageContent tmc = this.event.getMessage();
        String userId = this.event.getSource().getUserId();
        String text, category;

        String url = "jdbc:h2:~/h2db/softeng;AUTO_SERVER=TRUE;MODE=PostgreSQL";
        String user = "b2201820";
        String pass = "b2201820";

        //try {
        //    Connection connection = DriverManager.getConnection(url, user, pass);
        //} catch (SQLException throwables) {
        //    throwables.printStackTrace();
        //}

        String sql = "insert into reminder_item " +
                "(user_id, push_at, push_text, push_price ) " +
                "values(?,?,?,?) ";

        LocalDate localDate = LocalDate.now();

        if(tmc.getText().indexOf("食費") >= 0){
            category = "食費";
        }else if(tmc.getText().indexOf("日用品費") >= 0){
            category = "日用品費";
        }else if(tmc.getText().indexOf("光熱費") >= 0){
            category = "光熱費";
        }else if(tmc.getText().indexOf("その他") >= 0){
            category = "その他";
        }else if(tmc.getText().indexOf("累計") >= 0){
            category = "累計";
        }else{
            category = "else";
        }

        String strprice = tmc.getText().replaceAll("[^0-9]","");
        int  intprice = Integer.parseInt(strprice);


        try{
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,userId);
            ps.setDate(2, Date.valueOf(localDate));
            ps.setString(3,category);
            ps.setInt(4,intprice);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        text = "aa";
        return new TextMessage(text);
    }

}

