package com.example.managemyfridge;

public class Users {

        private int _id;
        private String username;
        private String email;
        private String password;
        private String img;
        private Product[] fridgeItems;

        public Users()
        {
            //empty constructor
        }
        public Users(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public Users(String username, String password, Product[] fridgeItems)
        {
            this.username = username;
            this.password = password;
            this.fridgeItems = fridgeItems;
        }

        public int getID() {
            return _id;
        }

        public void setID(int _id) {
            this._id = _id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email)
        {
            this.email=email;
        }
}
