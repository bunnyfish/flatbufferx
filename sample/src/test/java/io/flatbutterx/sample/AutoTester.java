package io.flatbutterx.sample;

//import io.flatbutterx.sample.loader.Main.RepoFB;
//import io.flatbutterx.sample.loader.Main.ReposListFB;

import io.flatbufferx.core.FlatBuffersX;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author bunnyblue
 */
public class AutoTester {
    @Test
    public void autoTest() {
        io.flatbutterx.sample.ReposListFB reposListFB = new io.flatbutterx.sample.ReposListFB();
        reposListFB.repos = new ArrayList<>();
        RepoFB repoFB = new RepoFB();
        repoFB.description = "description";
        repoFB.fullName = "fullname";
        repoFB.htmlUrl = "404";
        repoFB.name = "name";
        repoFB.id = 123L;
        UserFB owner = new UserFB();
        owner.id = 456L;
        owner.login = "login";
        repoFB.owner = owner;
        reposListFB.repos.add(repoFB);
        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = reposListFB.toFlatBuffer(reposListFB);
            System.err.println(new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReposList reposList = ReposList.getRootAsReposList(byteBuffer);
        System.out.println(reposList.reposLength());
        ReposListFB reposListFB1 = new ReposListFB();
        try {
            reposListFB1.flatBufferToBean(reposList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = FlatBuffersX.serialize(reposListFB1);
        //  reposListFB1
        //   String data=  reposListFB1.serialize(reposListFB1);
        System.out.println(data);
        ReposListFB reposListFB2 = new ReposListFB();
        try {
            ReposListFB reposListFB3 = reposListFB2.parse(data);
            System.out.println(reposListFB3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  RepoFB
    }
}
