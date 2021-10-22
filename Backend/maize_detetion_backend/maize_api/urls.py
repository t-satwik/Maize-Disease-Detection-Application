from django.urls import include, path

# from rest_framework import 

from maize_api.views import *

# router = routers.DefaultRouter()
# router.register(r'Data', DataViewSet)

urlpatterns = [
   path('SetData/', setData),
   path('UserLogin/', checkLogin), 
   path('UserSignUp/', newSignup),
   path('GetPastData/', getPastData)
]