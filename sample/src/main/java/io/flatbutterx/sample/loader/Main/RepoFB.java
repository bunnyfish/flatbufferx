package io.flatbutterx.sample.loader.Main;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.flatbuffers.FlatBufferBuilder;
import io.flatbufferx.core.FlatBuffersX;
import io.flatbufferx.core.FlatBufferMapper;
import io.flatbufferx.core.typeconverters.TypeConverter;
import io.flatbutterx.sample.Repo;

import java.io.IOException;
import java.nio.ByteBuffer;

@SuppressWarnings("unsafe,unchecked")
public final class RepoFB extends FlatBufferMapper<RepoFB> {
    private static TypeConverter<UserFB> io_flatbutterx_sample_User_type_converter;

    public String description;

    public String fullName;

    public String htmlUrl;

    public Long id;

    public String name;

    public UserFB owner;

    @Override
    public RepoFB parse(JsonParser jsonParser) throws IOException {
        RepoFB instance = new RepoFB();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    @Override
    public void parseField(RepoFB instance, String fieldName, JsonParser jsonParser) throws
            IOException {
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
        } else if ("fullName".equals(fieldName)) {
            instance.fullName = jsonParser.getValueAsString(null);
        } else if ("htmlUrl".equals(fieldName)) {
            instance.htmlUrl = jsonParser.getValueAsString(null);
        } else if ("id".equals(fieldName)) {
            instance.id = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf(jsonParser.getValueAsLong());
        } else if ("name".equals(fieldName)) {
            instance.name = jsonParser.getValueAsString(null);
        } else if ("owner".equals(fieldName)) {
            instance.owner = getio_flatbutterx_sample_User_type_converter().parse(jsonParser);
        }
    }

    @Override
    public void serialize(RepoFB object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws
            IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        if (object.fullName != null) {
            jsonGenerator.writeStringField("fullName", object.fullName);
        }
        if (object.htmlUrl != null) {
            jsonGenerator.writeStringField("htmlUrl", object.htmlUrl);
        }
        if (object.id != null) {
            jsonGenerator.writeNumberField("id", object.id);
        }
        if (object.name != null) {
            jsonGenerator.writeStringField("name", object.name);
        }
        if (object.owner != null) {
            getio_flatbutterx_sample_User_type_converter().serialize(object.owner, "owner", true, jsonGenerator);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }

    private static final TypeConverter<UserFB> getio_flatbutterx_sample_User_type_converter() {
        if (io_flatbutterx_sample_User_type_converter == null) {
            io_flatbutterx_sample_User_type_converter = FlatBuffersX.typeConverterFor(UserFB.class);
        }
        return io_flatbutterx_sample_User_type_converter;
    }

    @Override
    public ByteBuffer toFlatBuffer(RepoFB object) throws IOException {
        FlatBufferBuilder bufferBuilder = new FlatBufferBuilder();

        Repo.createRepo(bufferBuilder, object.id, bufferBuilder.createString(object.name), bufferBuilder.createString(object.fullName), object.owner.toFlatBufferOffset(bufferBuilder), bufferBuilder.createString(object.htmlUrl), bufferBuilder.createString(object.description));
        return bufferBuilder.dataBuffer();
    }

    @Override
    public int toFlatBufferOffset(FlatBufferBuilder bufferBuilder) throws IOException {

        int user = this.owner.toFlatBufferOffset(bufferBuilder);
        return Repo.createRepo(bufferBuilder, this.id, bufferBuilder.createString(this.name), bufferBuilder.createString(this.fullName),
                user, bufferBuilder.createString(this.htmlUrl), bufferBuilder.createString(this.description));
        //  return super.toFlatBufferOffset(bufferBuilder);
    }
//    @Override
//    public ByteBuffer toFlatBuffer(RepoFB object) throws IOException {
//        FlatBufferBuilder bufferBuilder = new FlatBufferBuilder();
//        Repos.Repo.createRepo(bufferBuilder,object.id,bufferBuilder.createString(object.name),bufferBuilder.createString(object.fullName),4,bufferBuilder.createString(object.htmlUrl),bufferBuilder.createString(object.description),object.fork,bufferBuilder.createString(object.url),bufferBuilder.createString(object.createdAt),bufferBuilder.createString(object.updatedAt),bufferBuilder.createString(object.pushedAt),bufferBuilder.createString(object.gitUrl),bufferBuilder.createString(object.sshUrl),bufferBuilder.createString(object.cloneUrl),bufferBuilder.createString(object.svnUrl),bufferBuilder.createString(object.homepage),object.size,object.stargazersCount,object.watchersCount,bufferBuilder.createString(object.language),object.hasIssues,object.hasDownloads,object.hasWiki,object.hasPages,object.forksCount,bufferBuilder.createString(object.mirrorUrl),object.openIssuesCount,object.forks,object.openIssues,object.watchers,bufferBuilder.createString(object.defaultBranch));
//        return bufferBuilder.dataBuffer();
//    }

    @Override
    public String toString() {
        return "RepoFB{" +
                "description='" + description + '\'' +
                ", fullName='" + fullName + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }

    @Override
    public RepoFB flatBufferToBean(Object object) throws IOException {
        Repo repo = (Repo) object;
        this.fullName = repo.fullName();
        this.htmlUrl = repo.htmlUrl();
        this.description = repo.description();
        this.id = repo.id();
        this.name = repo.name();
        UserFB owner = new UserFB();
        owner.flatBufferToBean(repo.owner());
        this.owner = owner;
        return this;

        // this.owner=repo.owner()
        //return super.flatBufferToBean(object);
    }


}
