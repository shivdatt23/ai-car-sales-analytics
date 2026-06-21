# =========================================================
# IMPORT LIBRARIES
# =========================================================

import streamlit as st
import requests
import pandas as pd
import plotly.express as px
import os

# =========================================================
# PAGE CONFIG
# =========================================================

st.set_page_config(
    page_title="Car Sales Dashboard",
    page_icon="🚗",
    layout="wide"
)

# =========================================================
# CUSTOM CSS
# =========================================================

st.markdown("""
<style>

.main {
    background-color: #0E1117;
}

.stMetric {
    background-color: #1E1E1E;
    padding: 15px;
    border-radius: 10px;
    text-align: center;
}

h1,h2,h3 {
    color: white;
}

</style>
""", unsafe_allow_html=True)


# =========================================================
# TITLE
# =========================================================

st.title("🚗Car Sales Analytics Dashboard")

st.markdown("### 📊 Real-time Analytics using Spring Boot + Streamlit")


# BASE_URL = "http://localhost:8081/api"
# we give the below URL because my backend is in different container
# BASE_URL = "http://host.docker.internal:8081/api"


# BASE_URL = "http://backend:8081/api"
# backend is the service name from docker-compose.yml, and Docker's internal network lets containers resolve each other by service name.

BASE_URL = os.getenv("BASE_URL", "http://localhost:8081/api")

# The second value http://backend:8081/api is a fallback so app still works when run it locally outside Docker.

# =========================================================
# API FUNCTIONS
# =========================================================

@st.cache_data
def get_yearly_sales():

    response = requests.get(
        f"{BASE_URL}/car-sales/yearly-count"
    )

    if response.status_code == 200:
        return pd.DataFrame(response.json()['data'])

    return pd.DataFrame()


@st.cache_data
def get_monthly_sales(year):

    response = requests.get(
        f"{BASE_URL}/car-sales/monthly-count",
        params={"year": year}
    )

    if response.status_code == 200:
        return pd.DataFrame(response.json()['data'])

    return pd.DataFrame()


def ask_ai(question):

    response = requests.post(
        f"{BASE_URL}/ai/ask",
        json=question
    )

    return response.text


# =========================================================
# SIDEBAR
# =========================================================

st.sidebar.title("📌 Navigation")

option = st.sidebar.radio(
    "Select Module",
    [
        "📈 Yearly Analytics",
        "📅 Monthly Analytics",
        "🤖 AI Insights"
    ]
)


# =========================================================
# YEARLY ANALYTICS
# =========================================================

if option == "📈 Yearly Analytics":

    st.subheader("📈 Yearly Car Sales Analysis")

    with st.spinner("Loading Data..."):
        df = get_yearly_sales()

    if df.empty:
        st.warning("No Data Available")
    else:

        total_sales = df['count'].sum()
        max_sales = df['count'].max()
        avg_sales = round(df['count'].mean(), 2)

        col1, col2, col3 = st.columns(3)

        col1.metric("🚗 Total Sales", total_sales)
        col2.metric("📈 Max Sales", max_sales)
        col3.metric("📊 Average Sales", avg_sales)

        st.markdown("---")

        fig = px.line(
            df,
            x="year",
            y="count",
            markers=True,
            text="count",
            title="Yearly Sales Trend"
        )

        fig.update_layout(template="plotly_dark", title_x=0.5)

        st.plotly_chart(fig, use_container_width=True)

        fig2 = px.bar(
            df,
            x="year",
            y="count",
            text="count",
            color="count",
            title="Year-wise Sales"
        )

        fig2.update_layout(template="plotly_dark", title_x=0.5)

        st.plotly_chart(fig2, use_container_width=True)

        st.dataframe(df, use_container_width=True)


# =========================================================
# MONTHLY ANALYTICS (UPDATED)
# =========================================================

elif option == "📅 Monthly Analytics":

    st.subheader("📅 Monthly Sales Analysis")

    year = st.number_input(
        "Select Year",
        min_value=2000,
        max_value=2100,
        value=2024
    )

    with st.spinner("Loading Monthly Data..."):
        df = get_monthly_sales(year)

    if df.empty:
        st.warning("No Data Found")
    else:

        month_map = {
            1: "Jan", 2: "Feb", 3: "Mar",
            4: "Apr", 5: "May", 6: "Jun",
            7: "Jul", 8: "Aug", 9: "Sep",
            10: "Oct", 11: "Nov", 12: "Dec"
        }

        df["month_name"] = df["month"].map(month_map)

        total_sales = df["count"].sum()

        st.metric("🚗 Total Sales", total_sales)

        st.markdown("---")

        fig = px.bar(
            df,
            x="month_name",
            y="count",
            text="count",
            color="count",
            title=f"Monthly Sales - {year}"
        )

        fig.update_layout(template="plotly_dark", title_x=0.5)

        st.plotly_chart(fig, use_container_width=True)

        fig2 = px.line(
            df,
            x="month_name",
            y="count",
            markers=True,
            text="count",
            title=f"Monthly Trend - {year}"
        )

        fig2.update_layout(template="plotly_dark", title_x=0.5)

        st.plotly_chart(fig2, use_container_width=True)

        st.dataframe(df, use_container_width=True)


# =========================================================
# AI INSIGHTS
# =========================================================

elif option == "🤖 AI Insights":

    st.subheader("🤖 AI Powered Car Sales Insights")

    question = st.text_area(
        "Ask Anything",
        placeholder="Example: Which year had highest sales?"
    )

    if st.button("Ask AI"):

        if question.strip() == "":
            st.warning("Please enter question")
        else:
            with st.spinner("AI Thinking..."):
                answer = ask_ai(question)

            st.success("AI Response")
            st.write(answer)


# =========================================================
# FOOTER
# =========================================================

st.markdown("---")

st.caption("🚀 Built with Streamlit + Spring Boot + Plotly | By Shivdatt Bibhar")