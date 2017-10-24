/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;
import java.io.FileInputStream;
import java.security.MessageDigest;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static javax.measure.unit.SI.KILOGRAM;
import javax.measure.quantity.Mass;
import org.jscience.physics.model.RelativisticModel;
import org.jscience.physics.amount.Amount;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.URISyntaxException;
import java.net.URI;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

    private static String UPLOADED_FOLDER = "/immages/";
    public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }
    public static String getMD5(String data) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");

        messageDigest.update(data.getBytes());
        byte[] digest=messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString((int) (b & 0xff)));
        }
        return sb.toString();
    }
  @RequestMapping("/")
  String index() {
    return "index";
  }
  @RequestMapping("/hello")
String hello(Map<String, Object> model) {
    RelativisticModel.select();
    Amount<Mass> m = Amount.valueOf("12 GeV").to(KILOGRAM);
    model.put("science", "E=mc^2: GeV = " + m.toString());
    return "hello";
}

  @RequestMapping("/db")
String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
}
    @RequestMapping("/dbo")
    String dbo(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user(id bigserial PRIMARY KEY,fname VARCHAR (250)  NOT NULL,lastname VARCHAR (250)  NOT NULL,username VARCHAR (250) UNIQUE NOT NULL,password VARCHAR (250) NOT NULL,email VARCHAR (355) UNIQUE NOT NULL,created_on TIMESTAMP NOT NULL,last_login TIMESTAMP);");
            stmt.executeUpdate("INSERT INTO con(firstname,lastname,email) VALUES ('ali','veli','sa@co')");
            ResultSet rs = stmt.executeQuery("SELECT * FROM con");

            ArrayList<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add("Read from db name: " + rs.getString("firstname"));
                output.add("Read from db name: " + rs.getString("lastname"));
                output.add("Read from db name: " + rs.getString("email"));

            }

            model.put("records", output);
            return "dbo";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value="/kayit", method= RequestMethod.POST)
    public String createContact(@ModelAttribute Kisi contact,
                                 Model model) {
        model.addAttribute("contact", contact);
        Map modelmap=model.asMap();
        String x="";
        ArrayList<String> output = new ArrayList<String>();
        int id = contact.getId();
        String timeStam="";
        String first = contact.getFirst();
        String last = contact.getLast();
        String username=contact.getUsername();
        String password=contact.getPassword();
        String  mspassword=null;
        try {
            mspassword = getMD5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        String email = contact.getEmail();
         timeStam = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
      /*  if(first.isEmpty()&&first==null){
            x="adınızı yazmadınız kaydınız tamamlanmadı";
            modelmap.put("records",x);
            return "result";
        }
        if(last.isEmpty()&&last==null){
            x="soyadınızı yazmadınız kaydınız tamamlanmadı";
            modelmap.put("records",x);
            return "result";
        }
        if(username.isEmpty()&&username==null){
            x="kullanıcı adınızı yazmadınızkaydınız tamamlanmadı ";
            modelmap.put("records",x);
            return "result";
        }
        if(password.isEmpty()&&password==null){
            x="şifrenizi yazmadınız kaydınız tamamlanmadı";
            modelmap.put("records",x);
            return "result";

        }
        if(email.isEmpty()&&email==null){
            x="soyadınızı yazmadınız kaydınız tamamlanmadı";
            modelmap.put("records",x);
            return "result";
        }*/






        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
           // stmt.executeUpdate("DROP TABLE con");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS uye(id bigserial PRIMARY KEY,fname VARCHAR (250)  NOT NULL,lastname VARCHAR (250)  NOT NULL,username VARCHAR (250) UNIQUE NOT NULL,password VARCHAR (250) NOT NULL,email VARCHAR (355) UNIQUE NOT NULL,created_on TIMESTAMP NOT NULL,last_login TIMESTAMP);");
          String sql;
            sql =
                    "insert into uye(fname, lastname,username,password, email,created_on,last_login) values " +
                            "('" + first  + "', '" + last + " ','" + username  + "','" + mspassword  + "',' " + email +  "',' " + timeStam + "',' " + timeStam +  "');";
            stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery("SELECT * FROM uye");


            while (rs.next()) {
                output.add("Read from db name: " + rs.getString("fname"));
                output.add("Read from db name: " + rs.getString("lastname"));
                output.add("Read from db name: " + rs.getString("email"));
                output.add("Read from db name: " + rs.getString("password"));
                output.add("Read from db name: " + rs.getString("created_on"));
                output.add("Read from db id: " + rs.getString("id"));

            }

            modelmap.put("records", output);
            return "result";
        } catch (Exception e) {
            modelmap.put("message", e.getMessage());
            return "error";
        }






    }
    @RequestMapping(value="/kayit", method= RequestMethod.GET)
    public String createContactk(@ModelAttribute Kisi contact,
                                Model model) {




        model.addAttribute("contact", contact);




        return "kayit";
    }

    @RequestMapping(value="/validate", method= RequestMethod.POST)
    public String createContactm(@ModelAttribute Kisi contact,
                                 Model model,HttpServletResponse response,HttpServletRequest request) {

        model.addAttribute("contact", contact);
        Map modelmap=model.asMap();
        String x="";
int idx=0;
        String username=contact.getUsername();
        String password=contact.getPassword();
        int id = contact.getId();
        String  mspassword=null;
        try {
            mspassword = getMD5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            // stmt.executeUpdate("DROP TABLE con");
            String sql="";
            sql="SELECT *FROM uye WHERE username='"+username+"' AND password='"+mspassword+"'";
            ResultSet rs = stmt.executeQuery(sql);
           /* Cookie newCookie = new Cookie("giris", "true");
            newCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(newCookie);*/
            ArrayList<String> output = new ArrayList<String>();
while (rs.next()){
    output.add("Read from db name: " + rs.getString("fname"));
     x=rs.getString("id");
    Cookie cookie = new Cookie("id", x);
      Cookie newCookie = new Cookie("giris", "true");

          //  newCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(newCookie);
            response.addCookie(cookie);
 /*   HttpSession session=request.getSession();
    String t="true";
    session.setAttribute("giris",t);
    session.setAttribute("userid",x);*/
    return "redirect:/upload";
}


            return "hata";
        } catch (Exception e) {
            modelmap.put("message", e.getMessage());
            return "hata";
        }




    }

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public String uploadd(@ModelAttribute Kisi contact,
                         Model model,HttpServletRequest request,@RequestParam("file") MultipartFile file,
                          RedirectAttributes redirectAttributes) {

        model.addAttribute("contact", contact);
        ArrayList<String> output = new ArrayList<String>();
        Map modelmap=model.asMap();

       Cookie[] cookies = request.getCookies();
       Cookie cook;
    /*  for (Cookie c : request.getCookies()) {
           if (c.getValue().equals("true"))
           { if(c.getName().equals("id"))
              modelmap.put("records",c.getValue());
               return "upload";}
           else
               return "hata";
       }*/

     //  HttpSession session=request.getSession();
       String x="";
     //  x=session.getAttribute("giris").toString();
       String y="aliveli";
       if (cookies!=null){
       for (int i=0;i<cookies.length;i++){
           cook=cookies[i];

           if (cook.getValue().equals("true")){
           for (int j=0;j<cookies.length;j++){
            //cookie gieis begin
               Cookie cook1=cookies[j];

            if (cook1.getName().equals("id")){

           y=cook1.getValue();

           if (file.isEmpty()) {
               redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
               return "redirect:uploadStatus";
           }
           try {

               // Get the file and save it somewhere
               byte[] bytes = file.getBytes();


               Path path = Paths.get("/stylesheets/"+ file.getOriginalFilename());
               output.add("path aasdad123"+path.toString());
            File f = new File("stylesheets");
            if(!f.exists())
            {
                output.add("path girdi"+path.toString());
                f.createNewFile();
                output.add("path oldu"+path.toString());
            }
            else output.add("path aasdad"+path.toString());

               Files.write(path, bytes);
               output.add("path You successfully uploaded"+path.toString());
                 redirectAttributes.addFlashAttribute("message",
                         "You successfully uploaded '" + file.getOriginalFilename() + "'");

           }
           catch (IOException e) {
              // e.printStackTrace();
               modelmap.put("records","path4");
               redirectAttributes.addFlashAttribute("message",e);
               return "redirect:/resultupload";
           }

           try (Connection connection = dataSource.getConnection()) {
               Statement stmt = connection.createStatement();
               // stmt.executeUpdate("DROP TABLE con");
               String sql="";
               sql="SELECT *FROM uye WHERE id='"+y+"'";
               ResultSet rs = stmt.executeQuery(sql);
          /* Cookie newCookie = new Cookie("giris", "true");
           newCookie.setMaxAge(24 * 60 * 60);
           response.addCookie(newCookie);*/

               while (rs.next()){

          //         output.add("Read from db name: " + rs.getString("fname"));
           //        x=rs.getString("id");
                   //Cookie cookie = new Cookie("id", x);
                   //Cookie newCookie = new Cookie("giris", "true");

                   //  newCookie.setMaxAge(24 * 60 * 60);
                   ///    response.addCookie(newCookie);
                   // response.addCookie(cookie);

              //     output.add("y degeri"+y);
                //   output.add("girilmedi");
                  // modelmap.put("records",output);
                  // return "redirect:/upload";
               }
              // output.add("girildi")
               // output.add("y degeri"+y);
               modelmap.put("records",output);

             //  return "upload";
           } catch (Exception e) {
               modelmap.put("message", e.getMessage());
               return "redirect:/resultupload";
           }

                return "resultupload";

           }

           } //cookie gieris end


           }


       }}



      return "hata";


    }
    @RequestMapping(value="/resultupload", method= RequestMethod.GET)
    public String uploadStatus(@ModelAttribute String records, Model model ) {
        model.addAttribute("records", records);
        Map modelmap=model.asMap();

        return "resultupload";
    }
    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public String upload(@ModelAttribute Kisi contact,
                                 Model model,HttpServletRequest request) {
        model.addAttribute("contact", contact);
        Map modelmap=model.asMap();
        Cookie[] cookies = request.getCookies();
        Cookie cook;


        //  HttpSession session=request.getSession();
        String x="";
        //  x=session.getAttribute("giris").toString();
        String y="aliveli";
        if (cookies!=null){
            for (int i=0;i<cookies.length;i++){
                cook=cookies[i];
                if (cook.getValue().equals("true")){ //cookie gieis begin
                    for (int j=0;j<cookies.length;j++){

                        cook=cookies[j];
                        if (cook.getName().equals("id")){

                            y=cook.getValue();
                 /*           if (file.isEmpty()) {
                                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
                                return "redirect:uploadStatus";
                            }
                            try {

                                // Get the file and save it somewhere
                                byte[] bytes = file.getBytes();
                                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                                Files.write(path, bytes);

                                redirectAttributes.addFlashAttribute("message",
                                        "You successfully uploaded '" + file.getOriginalFilename() + "'");

                            } catch (IOException e) {
                                // e.printStackTrace();
                                redirectAttributes.addFlashAttribute("message",e);
                                return "redirect:/resultupload";
                            }*/

                            try (Connection connection = dataSource.getConnection()) {
                                Statement stmt = connection.createStatement();
                                // stmt.executeUpdate("DROP TABLE con");
                                String sql="";
                                sql="SELECT *FROM uye WHERE id='"+y+"'";
                                ResultSet rs = stmt.executeQuery(sql);

                                ArrayList<String> output = new ArrayList<String>();
                                while (rs.next()){

                                    //         output.add("Read from db name: " + rs.getString("fname"));
                                    //        x=rs.getString("id");
                                    //Cookie cookie = new Cookie("id", x);
                                    //Cookie newCookie = new Cookie("giris", "true");

                                    //  newCookie.setMaxAge(24 * 60 * 60);
                                    ///    response.addCookie(newCookie);
                                    // response.addCookie(cookie);

                                    //     output.add("y degeri"+y);
                                    //   output.add("girilmedi");
                                    // modelmap.put("records",output);
                                    // return "redirect:/upload";
                                }
                                // output.add("girildi")
                                // output.add("y degeri"+y);
                                modelmap.put("records",output);

                                //  return "upload";
                            } catch (Exception e) {
                                modelmap.put("message", e.getMessage());
                                return "redirect:/resultupload";
                            }

                        }
                        return "upload";

                    } //cookie gieris end


                }


            }}



        return "hata";

    }


    @RequestMapping(value="/validate", method= RequestMethod.GET)
    public String createContactl(@ModelAttribute Kisi contact,
                                 Model model) {

        model.addAttribute("contact", contact);

        return "validate";
    }


    @Bean
public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
}
}
