package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.yuyr757.pojo.Blog;
import org.yuyr757.utils.IDutils;
import org.yuyr757.utils.MybatisUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogMapperTest {
    @Test
    public void testAddBlog(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
            Blog blog1 = new Blog(IDutils.getID(), "title-1", "name-1", new Date(System.currentTimeMillis()), (int) (Math.random()*100));
            Blog blog2 = new Blog(IDutils.getID(), "title-2", "name-2", new Date(System.currentTimeMillis()), (int) (Math.random()*100));
            Blog blog3 = new Blog(IDutils.getID(), "title-3", "name-3", new Date(System.currentTimeMillis()), (int) (Math.random()*100));
            Blog blog4 = new Blog(IDutils.getID(), "title-4", "name-4", new Date(System.currentTimeMillis()), (int) (Math.random()*100));
            Blog blog5 = new Blog(IDutils.getID(), "title-5", "name-5", new Date(System.currentTimeMillis()), (int) (Math.random()*100));
            blogMapper.addBlog(blog1);
            blogMapper.addBlog(blog2);
            blogMapper.addBlog(blog3);
            blogMapper.addBlog(blog4);
            blogMapper.addBlog(blog5);
        }
    }

    @Test
    public void testGetBlogList(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
            Map<String, String> map = new HashMap<>();
            map.put("title", "title");
            map.put("author", "name-2");
            List<Blog> blogList = blogMapper.getBlogList(map);
            for (Blog blog : blogList) {
                System.out.println(blog);
            }
        }
    }
}
