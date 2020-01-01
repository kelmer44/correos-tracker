package net.kelmer.correostracker.util.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

public class SingleToArrayAdapter extends JsonAdapter<List<Object>> {
    final JsonAdapter<List<Object>> delegateAdapter;
    final JsonAdapter<Object> elementAdapter;


    public static final Factory FACTORY = new Factory() {
        @Nullable
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations,
                                     Moshi moshi) {
            Set<? extends Annotation> delegateAnnotations =
                    Types.nextAnnotations(annotations, SingleToArray.class);
            if (delegateAnnotations == null) {
                return null;
            }
            if (Types.getRawType(type) != List.class) {
                throw new IllegalArgumentException(
                        "Only lists may be annotated with @SingleToArray. Found: " + type);
            }
            Type elementType = Types.collectionElementType(type, List.class);
            JsonAdapter<List<Object>> delegateAdapter = moshi.adapter(type, delegateAnnotations);
            JsonAdapter<Object> elementAdapter = moshi.adapter(elementType);
            return new SingleToArrayAdapter(delegateAdapter, elementAdapter);
        }
    };

    public SingleToArrayAdapter(JsonAdapter<List<Object>> delegateAdapter, JsonAdapter<Object> elementAdapter) {
        this.delegateAdapter = delegateAdapter;
        this.elementAdapter = elementAdapter;
    }

    @Nullable @Override public List<Object> fromJson(JsonReader reader) throws IOException {
        if (reader.peek() != JsonReader.Token.BEGIN_ARRAY) {
            return Collections.singletonList(elementAdapter.fromJson(reader));
        }
        return delegateAdapter.fromJson(reader);
    }

    @Override public void toJson(JsonWriter writer, @Nullable List<Object> value)
            throws IOException {
        if (value.size() == 1) {
            elementAdapter.toJson(writer, value.get(0));
        } else {
            delegateAdapter.toJson(writer, value);
        }
    }
}