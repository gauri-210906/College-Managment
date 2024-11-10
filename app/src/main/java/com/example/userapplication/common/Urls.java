package com.example.userapplication.common;

public class Urls {

    public static String webServiceAddress = "http://192.168.1.8:80/CollegeAPI/";
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
    public static String getAllDepartmentTeacherWebService = webServiceAddress + "getAllDepartmentDetails.php";
    public static String getAllDepartmentWiseYearList = webServiceAddress + "departmentwiseyear.php";
    public static String getAllStudentCurrentLocation = webServiceAddress + "getAllStudentLocation.php";
    public static String getYearWiseStudentsWebService = webServiceAddress + "yearWiseStudentsList.php";
    public static String showStudentWiseDetailsWebService = webServiceAddress + "showstudentwisedetails.php";
    public static String studentAddProfilePhotoWebService = webServiceAddress + "addStudentRegisterImage.php";
    public static String teacherAddProfilePhotoWebService = webServiceAddress + "addTeacherProfilePhoto.php";
    public static String teacherRegisterWebService = webServiceAddress + "teacherRegister.php";

}
