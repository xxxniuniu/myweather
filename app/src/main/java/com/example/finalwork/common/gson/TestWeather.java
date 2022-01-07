package com.example.finalwork.common.gson;

import java.io.Serializable;
import java.util.List;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class TestWeather {

    @com.fasterxml.jackson.annotation.JsonProperty("cityid")
    private String cityid;
    @com.fasterxml.jackson.annotation.JsonProperty("city")
    private String city;
    @com.fasterxml.jackson.annotation.JsonProperty("cityEn")
    private String cityEn;
    @com.fasterxml.jackson.annotation.JsonProperty("country")
    private String country;
    @com.fasterxml.jackson.annotation.JsonProperty("countryEn")
    private String countryEn;
    @com.fasterxml.jackson.annotation.JsonProperty("update_time")
    private String update_time;
    @com.fasterxml.jackson.annotation.JsonProperty("data")
    private List<DataDTO> data;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String updateTime) {
        this.update_time = updateTime;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDTO implements Serializable {
        @com.fasterxml.jackson.annotation.JsonProperty("day")
        private String day;
        @com.fasterxml.jackson.annotation.JsonProperty("date")
        private String date;
        @com.fasterxml.jackson.annotation.JsonProperty("week")
        private String week;
        @com.fasterxml.jackson.annotation.JsonProperty("wea")
        private String wea;
        @com.fasterxml.jackson.annotation.JsonProperty("wea_img")
        private String wea_img;
        @com.fasterxml.jackson.annotation.JsonProperty("wea_day")
        private String weaDay;
        @com.fasterxml.jackson.annotation.JsonProperty("wea_day_img")
        private String weaDayImg;
        @com.fasterxml.jackson.annotation.JsonProperty("wea_night")
        private String weaNight;
        @com.fasterxml.jackson.annotation.JsonProperty("wea_night_img")
        private String weaNightImg;
        @com.fasterxml.jackson.annotation.JsonProperty("tem")
        private String tem;
        @com.fasterxml.jackson.annotation.JsonProperty("tem1")
        private String tem1;
        @com.fasterxml.jackson.annotation.JsonProperty("tem2")
        private String tem2;
        @com.fasterxml.jackson.annotation.JsonProperty("humidity")
        private String humidity;
        @com.fasterxml.jackson.annotation.JsonProperty("visibility")
        private String visibility;
        @com.fasterxml.jackson.annotation.JsonProperty("pressure")
        private String pressure;
        @com.fasterxml.jackson.annotation.JsonProperty("win_speed")
        private String win_speed;
        @com.fasterxml.jackson.annotation.JsonProperty("win_meter")
        private String winMeter;
        @com.fasterxml.jackson.annotation.JsonProperty("sunrise")
        private String sunrise;
        @com.fasterxml.jackson.annotation.JsonProperty("sunset")
        private String sunset;
        @com.fasterxml.jackson.annotation.JsonProperty("air")
        private String air;
        @com.fasterxml.jackson.annotation.JsonProperty("air_level")
        private String air_level;
        @com.fasterxml.jackson.annotation.JsonProperty("air_tips")
        private String air_tips;
        @com.fasterxml.jackson.annotation.JsonProperty("alarm")
        private AlarmDTO alarm;
        @com.fasterxml.jackson.annotation.JsonProperty("win")
        private List<String> win;
        @com.fasterxml.jackson.annotation.JsonProperty("hours")
        private List<HoursDTO> hours;
        @com.fasterxml.jackson.annotation.JsonProperty("index")
        private List<IndexDTO> index;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWea() {
            return wea;
        }

        public void setWea(String wea) {
            this.wea = wea;
        }

        public String getWeaImg() {
            return wea_img;
        }

        public void setWeaImg(String weaImg) {
            this.wea_img = weaImg;
        }

        public String getWeaDay() {
            return weaDay;
        }

        public void setWeaDay(String weaDay) {
            this.weaDay = weaDay;
        }

        public String getWeaDayImg() {
            return weaDayImg;
        }

        public void setWeaDayImg(String weaDayImg) {
            this.weaDayImg = weaDayImg;
        }

        public String getWeaNight() {
            return weaNight;
        }

        public void setWeaNight(String weaNight) {
            this.weaNight = weaNight;
        }

        public String getWeaNightImg() {
            return weaNightImg;
        }

        public void setWeaNightImg(String weaNightImg) {
            this.weaNightImg = weaNightImg;
        }

        public String getTem() {
            return tem;
        }

        public void setTem(String tem) {
            this.tem = tem;
        }

        public String getTem1() {
            return tem1;
        }

        public void setTem1(String tem1) {
            this.tem1 = tem1;
        }

        public String getTem2() {
            return tem2;
        }

        public void setTem2(String tem2) {
            this.tem2 = tem2;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getWinSpeed() {
            return win_speed;
        }

        public void setWinSpeed(String winSpeed) {
            this.win_speed = winSpeed;
        }

        public String getWinMeter() {
            return winMeter;
        }

        public void setWinMeter(String winMeter) {
            this.winMeter = winMeter;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getAir() {
            return air;
        }

        public void setAir(String air) {
            this.air = air;
        }

        public String getAirLevel() {
            return air_level;
        }

        public void setAirLevel(String airLevel) {
            this.air_level = airLevel;
        }

        public String getAirTips() {
            return air_tips;
        }

        public void setAirTips(String airTips) {
            this.air_tips = airTips;
        }

        public AlarmDTO getAlarm() {
            return alarm;
        }

        public void setAlarm(AlarmDTO alarm) {
            this.alarm = alarm;
        }

        public List<String> getWin() {
            return win;
        }

        public void setWin(List<String> win) {
            this.win = win;
        }

        public List<HoursDTO> getHours() {
            return hours;
        }

        public void setHours(List<HoursDTO> hours) {
            this.hours = hours;
        }

        public List<IndexDTO> getIndex() {
            return index;
        }

        public void setIndex(List<IndexDTO> index) {
            this.index = index;
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class AlarmDTO implements Serializable {
            @com.fasterxml.jackson.annotation.JsonProperty("alarm_type")
            private String alarmType;
            @com.fasterxml.jackson.annotation.JsonProperty("alarm_level")
            private String alarmLevel;
            @com.fasterxml.jackson.annotation.JsonProperty("alarm_content")
            private String alarmContent;

            public String getAlarmType() {
                return alarmType;
            }

            public void setAlarmType(String alarmType) {
                this.alarmType = alarmType;
            }

            public String getAlarmLevel() {
                return alarmLevel;
            }

            public void setAlarmLevel(String alarmLevel) {
                this.alarmLevel = alarmLevel;
            }

            public String getAlarmContent() {
                return alarmContent;
            }

            public void setAlarmContent(String alarmContent) {
                this.alarmContent = alarmContent;
            }
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class HoursDTO implements Serializable {
            @com.fasterxml.jackson.annotation.JsonProperty("hours")
            private String hours;
            @com.fasterxml.jackson.annotation.JsonProperty("wea")
            private String wea;
            @com.fasterxml.jackson.annotation.JsonProperty("wea_img")
            private String weaImg;
            @com.fasterxml.jackson.annotation.JsonProperty("tem")
            private String tem;
            @com.fasterxml.jackson.annotation.JsonProperty("win")
            private String win;
            @com.fasterxml.jackson.annotation.JsonProperty("win_speed")
            private String winSpeed;

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getWea() {
                return wea;
            }

            public void setWea(String wea) {
                this.wea = wea;
            }

            public String getWeaImg() {
                return weaImg;
            }

            public void setWeaImg(String weaImg) {
                this.weaImg = weaImg;
            }

            public String getTem() {
                return tem;
            }

            public void setTem(String tem) {
                this.tem = tem;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getWinSpeed() {
                return winSpeed;
            }

            public void setWinSpeed(String winSpeed) {
                this.winSpeed = winSpeed;
            }
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class IndexDTO implements Serializable {
            @com.fasterxml.jackson.annotation.JsonProperty("title")
            private String title;
            @com.fasterxml.jackson.annotation.JsonProperty("level")
            private String level;
            @com.fasterxml.jackson.annotation.JsonProperty("desc")
            private String desc;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
