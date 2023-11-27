import streamlit as st
import pandas as pd
from md5algorithm import md5


def md5_hash(text):
    return md5(text)

def update_csv(email, password):
    email_hash = md5_hash(email)
    password_hash = md5_hash(password)
    both_hash = md5_hash(email_hash + password_hash)
    new_row = {
        'email': email,
        'password': password,
        'emailhash': email_hash,
        'passwordhash': password_hash,
        'bothhash': both_hash
    }
    
    new_df = pd.DataFrame([new_row])
    updated_df = pd.concat([new_df, pd.read_csv('emailpassword.csv')])
    updated_df.to_csv('adsfsd.csv', index=False)


def main():
    st.title("Login and Signup App")
    df = pd.read_csv('emailpassword.csv')
    
    login = st.checkbox("Login")
    
    if login:
        email = st.text_input("Email")
        password = st.text_input("Password", type="password")
        if st.button("Submit"):
            email_hash = md5_hash(email)
            password_hash = md5_hash(password)
            both_hash = md5_hash(email_hash + password_hash)
            
            if df[(df['emailhash'] == email_hash) & (df['passwordhash'] == password_hash) & (df['bothhash'] == both_hash)].shape[0] > 0:  # noqa: E501
                st.success("You're logged in!")
            else:
                st.warning("Login failed. Sign up or check your credentials.")
                # display saying that the hash is not matching and specify which hash is not matching   # noqa: E501
                if df[df['emailhash'] != email_hash].shape[0] > 0:
                    st.warning("Email hash does not match.")
                if df[df['passwordhash'] != password_hash].shape[0] > 0:
                    st.warning("Password hash does not match.")
                if df[df['bothhash'] != both_hash].shape[0] > 0:
                    st.warning("Both hash does not match.")
    
    else:
        new_email = st.text_input("New Email")
        new_password = st.text_input("New Password", type="password")
        if st.button("Sign Up"):
            update_csv(new_email, new_password)
            st.success("Sign up successful. You can now log in.")

if __name__ == "__main__":
    main()