package hashcode;

/**
 * <pre>
 *
 *  File: HashcodeTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/26				lijunjun				Initial.
 *
 * </pre>
 */
public class HashcodeTest {

    public static void main(String[] args) {
        String str1 = "abc";
        String str2 = "abc";

        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));

        System.out.println("===========================");
        str1 = "abc";
        str2 = new String("abc");
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));

        System.out.println("===========================");
        str1 = new String("abc");
        str2 = new String("abc");
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));

        System.out.println("===========================");
        User user = new User("hashcode", 1, "431025101010112030");
        System.out.println(user.hashCode());
        System.out.println(System.identityHashCode(user));
    }

    static class User{
        String name;
        int gender;
        String identityNo;

        public User(String name, int gender, String identityNo) {
            this.name = name;
            this.gender = gender;
            this.identityNo = identityNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getIdentityNo() {
            return identityNo;
        }

        public void setIdentityNo(String identityNo) {
            this.identityNo = identityNo;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null){
                return false;
            }else if (obj instanceof User){
                User other = (User)obj;
                if(other.getIdentityNo() != null
                        && !"".equals(other.getIdentityNo())
                        && other.getIdentityNo().equals(this.identityNo)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            hash += identityNo.hashCode() * 13;
            hash = (hash >>> 1) | 0x0f;
            return hash;
        }
    }
}
