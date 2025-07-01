package com.teamgames.lib.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Gson TypeAdapter for java.util.Date, java.sql.Date, and Timestamp.
 */


public final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

  private final DateFormat enUsFormat;
  private final DateFormat localFormat;

  public DefaultDateTypeAdapter() {
    this(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
            DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
  }

  public DefaultDateTypeAdapter(String datePattern) {
    this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
  }

  public DefaultDateTypeAdapter(int style) {
    this(DateFormat.getDateInstance(style, Locale.US),
            DateFormat.getDateInstance(style));
  }

  public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
    this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US),
            DateFormat.getDateTimeInstance(dateStyle, timeStyle));
  }

  private DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
    this.enUsFormat = Objects.requireNonNull(enUsFormat);
    this.localFormat = Objects.requireNonNull(localFormat);
  }

  @Override
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    synchronized (localFormat) {
      return new JsonPrimitive(enUsFormat.format(src));
    }
  }

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {

    if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
      throw new JsonParseException("Expected a string for date, got: " + json);
    }

    Date date = deserializeToDate(json.getAsString());

    if (typeOfT == Date.class) {
      return date;
    } else if (typeOfT == Timestamp.class) {
      return new Timestamp(date.getTime());
    } else if (typeOfT == java.sql.Date.class) {
      return new java.sql.Date(date.getTime());
    } else {
      throw new JsonParseException("Cannot deserialize to " + typeOfT);
    }
  }

  private Date deserializeToDate(String json) {
    synchronized (localFormat) {
      try {
        return localFormat.parse(json);
      } catch (ParseException ignored) {}

      try {
        return enUsFormat.parse(json);
      } catch (ParseException ignored) {}

      try {
        return ISO8601Utils.parse(json, new ParsePosition(0));
      } catch (ParseException e) {
        throw new JsonSyntaxException(json, e);
      }
    }
  }

  @Override
  public String toString() {
    return DefaultDateTypeAdapter.class.getSimpleName() + '(' + localFormat.getClass().getSimpleName() + ')';
  }
}
