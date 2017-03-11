package com.yph.beauty.bean;

import java.util.List;

/**
 * Created by yph
 * Time is 2016/11/30 17:13
 * Good Good Study, Day Day Up
 *
 * 设计师-推荐页面实体类
 */

public class DesignerInfo {
    private DataBean data;
    private int result;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RecommendInfo{" +
                "data=" + data +
                ", result=" + result +
                '}';
    }

    public static class DataBean {
        private int has_next;
        private List<CategoriesBean> categories;
        private List<DesignersBean> designers;

        public int getHas_next() {
            return has_next;
        }

        public void setHas_next(int has_next) {
            this.has_next = has_next;
        }

        public List<CategoriesBean> getCategories() {
            return categories;
        }

        public void setCategories(List<CategoriesBean> categories) {
            this.categories = categories;
        }

        public List<DesignersBean> getDesigners() {
            return designers;
        }

        public void setDesigners(List<DesignersBean> designers) {
            this.designers = designers;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "has_next=" + has_next +
                    ", categories=" + categories +
                    ", designers=" + designers +
                    '}';
        }

        public static class CategoriesBean {
            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "CategoriesBean{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

        public static class DesignersBean {
            private String avatar_url;
            private String city;
            private String concept;
            private int follow_num;
            private int id;
            private int is_followed;
            private String label;
            private String name;
            private List<CategoriesBeanX> categories;
            private List<String> recommend_images;

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getConcept() {
                return concept;
            }

            public void setConcept(String concept) {
                this.concept = concept;
            }

            public int getFollow_num() {
                return follow_num;
            }

            public void setFollow_num(int follow_num) {
                this.follow_num = follow_num;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_followed() {
                return is_followed;
            }

            public void setIs_followed(int is_followed) {
                this.is_followed = is_followed;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CategoriesBeanX> getCategories() {
                return categories;
            }

            public void setCategories(List<CategoriesBeanX> categories) {
                this.categories = categories;
            }

            public List<String> getRecommend_images() {
                return recommend_images;
            }

            public void setRecommend_images(List<String> recommend_images) {
                this.recommend_images = recommend_images;
            }

            @Override
            public String toString() {
                return "DesignersBean{" +
                        "avatar_url='" + avatar_url + '\'' +
                        ", city='" + city + '\'' +
                        ", concept='" + concept + '\'' +
                        ", follow_num=" + follow_num +
                        ", id=" + id +
                        ", is_followed=" + is_followed +
                        ", label='" + label + '\'' +
                        ", name='" + name + '\'' +
                        ", categories=" + categories +
                        ", recommend_images=" + recommend_images +
                        '}';
            }

            public static class CategoriesBeanX {
                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                @Override
                public String toString() {
                    return "CategoriesBeanX{" +
                            "id=" + id +
                            ", name='" + name + '\'' +
                            '}';
                }
            }

        }
    }
}
