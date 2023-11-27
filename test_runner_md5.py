import md5algorithm as md5algorithm
import streamlit as st

# create a streamlit ask for the input message and then print the hash
st.title("MD5 Algorithm")
st.header("Enter the message")
message = st.text_input("Message")

if message:
    k = md5algorithm.md5(message)
    # return like the message and its hash is this beautiful way
    st.write("Message: ", message)
    st.write("Hash: ", k)