package com.example.userapplication.common;

public class Urls {

    public static String webServiceAddress = "http://192.168.1.6:80/CollegeAPI/";
    public static String loginUserWebService = webServiceAddress +"userlogin.php";
    public static String registerUserWebService =  webServiceAddress+"userregisterdetails.php";
    public static String forgetPasswordWebService = webServiceAddress + "userforgetpassword.php";
    public static String myDetailsWebService = webServiceAddress + "myDetails.php";
    public static String teacherGetMyDetailsWebService = webServiceAddress + "teacherMyDetails.php";
    public static String getAllTaskDetailsWebService = webServiceAddress + "getAllTaskDetails.php";
    public static String teacherLoginWebService = webServiceAddress + "teacherLogin.php";
    public static String teacherForgetPasswordWebService = webServiceAddress + "teacherForgetPassword.php";
    public static String updateProfileStudentWebService = webServiceAddress + "updateStudentProfile.php";
    public static String updateProfileTeacherWebService = webServiceAddress + "updateTeacherProfile.php";

}
