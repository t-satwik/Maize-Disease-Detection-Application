from django.contrib import admin
from maize_api.models import *
from django.contrib.admin.templatetags.admin_urls import admin_urlname
from django.shortcuts import resolve_url
from django.utils.html import format_html
from django.utils.safestring import SafeText

admin.site.site_header = "Maize Disease Detection Admin Dashboard"

def model_admin_url(obj, name=None) -> str:
    url = resolve_url(admin_urlname(obj._meta, SafeText("change")), obj.pk)
    return format_html('<a href="{}">{}</a>', url, name or str(obj))


@admin.register(User)
class User(admin.ModelAdmin):
    list_display = ("user_name", "email", "password")
    search_fields = ("user_name", "email")
    ordering = ("user_name", "email")

@admin.register(Data)
class Data(admin.ModelAdmin):
    list_display = ("time_stamp", "latitude", "longitude", "user_name", "predicted_class", "image_path", "crop_type")
    search_fields = ("user_name", "predicted_class", "crop_type")
    ordering = ("time_stamp", "user_name")

@admin.register(VideoFrame)
class VideoFrame(admin.ModelAdmin):
    list_display = ("time_stamp", "startLatitude", "startLongitude", "endLatitude", "endLongitude", "user_name", "encoded_image", "predicted_class", "image_path", "crop_type")
    search_fields = ("user_name", "predicted_class", "crop_type")
    ordering = ("time_stamp", "user_name")

@admin.register(Admin)
class Admin(admin.ModelAdmin):
    list_display = ("user_name", "email", "password")
    search_fields = ("user_name", "email")
    ordering = ("user_name", "email")