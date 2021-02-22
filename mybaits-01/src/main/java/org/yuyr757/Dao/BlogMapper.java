package org.yuyr757.Dao;

import org.yuyr757.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    public void addBlog(Blog blog);
    public List<Blog> getBlogList(Map<String, String> map);
}
