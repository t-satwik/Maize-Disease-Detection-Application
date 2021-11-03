
from rest_framework import status

# from maize_api.serializers import DataSerializer
from maize_api.models import Data
from maize_api.models import User
from maize_api.models import Admin
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
import sys 
import base64
import hashlib
from django.shortcuts import render, redirect
from django.contrib import messages

@api_view(['POST'])
def setData(request):
    try:
        data = request.data
        data_obj = Data()
        data_obj.time_stamp = data['time_stamp']
        time_stamp_str=str(data['time_stamp'])
        data_obj.latitude = data['latitude']
        data_obj.longitude = data['longitude']
        user_obj=User.objects.filter(user_name=data['user_name'])
        data_obj.user_name = user_obj[0]
        data_obj.predicted_class = data['predicted_class']
        data_obj.crop_type = data['crop_type']

        encoded_image_str=str(data['encoded_image'])
        data_obj.encoded_image = encoded_image_str

        imgdata = base64.b64decode(encoded_image_str) 
        # filename = '{}_{}.png'.format(data['user_id'], data['time_stamp'])
        time_stamp_str=time_stamp_str=str(data['time_stamp'])
        time_stamp_str=time_stamp_str.replace('/', '-')
        time_stamp_str=time_stamp_str.replace(' ', '_')
        time_stamp_str=time_stamp_str.replace(':', '-')

        user_name_str=str(data['user_name'])
        user_name_str=user_name_str.replace('/', '-')
        user_name_str=user_name_str.replace(' ', '_')
        time_stamp_str=time_stamp_str.replace(':', '-')

        imagePath=str('D:/Semester V/RnD/Backend/maize_detetion_backend/uploads/'+time_stamp_str+'_'+user_name_str+'_'+data['crop_type']+'.png')

        with open(imagePath, 'wb') as f:
            f.write(imgdata)
        data_obj.image_path=imagePath
        data_obj.save()
        return Response({"message":"Data Sent"}, status=status.HTTP_200_OK )

    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request - python Exception"}, status=status.HTTP_400_BAD_REQUEST )
        # return Response({"message":'uploads/'+time_stamp_str+'_'+user_id_str+'.png'}, status=status.HTTP_400_BAD_REQUEST )

@api_view(['POST'])
def checkLogin(request):
    try:
        data=request.data
        req_user_name=data['user_name']
        req_password=data['password']
        user = get_object_or_404(User, user_name=req_user_name)
        print(req_user_name, req_password, user.password)
        
        if 'password' in data:
            if(req_password == user.password):
                return Response({'message':"User Verified"}, status=status.HTTP_200_OK)
            else:
                return Response({'message':"Wrong Password"}, status=status.HTTP_401_UNAUTHORIZED)

    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request"}, status=status.HTTP_400_BAD_REQUEST )

@api_view(['POST'])
def newSignup(request):
    try:
        data = request.data
        user_obj = User()
        user_obj.user_name = data['user_name']
        user_obj.password=data['password']
        user_obj.email=data['email']
        user_obj.save()
        return Response({"message":"User Created"}, status=status.HTTP_200_OK )

    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request - python Exception"}, status=status.HTTP_400_BAD_REQUEST )

@api_view(['POST'])
def getPastData(request):
    try:
        data = request.data
        user_name = data['user_name']
        user_data=Data.objects.filter(user_name__exact=user_name)
        print(user_data)
        Response_dict={"message":"Data Fetch Successful", "data_count":str(len(user_data))}
        for i in range(len(user_data)):
            Response_dict["data"+str(i)]={}
            Response_dict["data"+str(i)]["time_stamp"]=user_data[i].time_stamp
            Response_dict["data"+str(i)]["latitude"]=user_data[i].latitude
            Response_dict["data"+str(i)]["longitude"]=user_data[i].longitude
            Response_dict["data"+str(i)]["image_path"]=user_data[i].encoded_image
            Response_dict["data"+str(i)]["predicted_class"]=user_data[i].predicted_class
            Response_dict["data"+str(i)]["crop_type"]=user_data[i].crop_type

        return Response(Response_dict, status=status.HTTP_200_OK )
    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request - python Exception"}, status=status.HTTP_400_BAD_REQUEST )
    
def developerView(request):
    # context ={
    #     "data":"Gfg is the best",
    #     "list":[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    # }
    # return response with template and context
    return render(request, 'developer/developer_home.html')



def devLogin(request):
    if request.method=='POST':
        try:
            username=request.POST['username']
            password=(hashlib.md5(request.POST['pass'].encode('utf-8')).hexdigest())
            print(password)
            admin_obj = get_object_or_404(Admin, user_name=username)
            if admin_obj is not None:
                if(password == admin_obj.password):
                    messages.success(request, "Admin Verified")
                    return redirect('/data/developer/home')
                else:
                    messages.success(request, "invalid credentials")
                    return redirect('/data/developer/login')
            else:
                messages.success(request, "invalid credentials")
                return redirect('/data/developer/login')
        except Exception:
            print(sys.exc_info())
            return redirect('/data/developer/login')
    else:
        return render(request, 'developer/login.html')
    
def devHome(request):

    user_name=request.POST.get('username', False)
    predicted_class=request.POST.get('predicted_class', False)
    crop_type=request.POST.get('crop_type', False)
    latitude=float(request.POST.get('latitude', False))
    longitude=float(request.POST.get('longitude', False))
    offset=float(request.POST.get('offset', False))
    # print(user_name)
    if(user_name):
        print("user_name search")
        data=Data.objects.filter(user_name__exact=user_name)
    elif(predicted_class):
        print("predicted_class search")
        data=Data.objects.filter(predicted_class__exact=predicted_class)     
    elif(crop_type):
        print("crop_type search")
        data=Data.objects.filter(crop_type__exact=crop_type)
    else:
        print("location search")
        data=Data.objects.filter(latitude__lte=latitude+offset, latitude__gte=latitude-offset, longitude__lte=longitude+offset, longitude__gte=longitude-offset)
    
    response_data=[]
    for i in range(len(data)):
            data_dict={}
            data_dict["s_no"]=i+1
            data_dict["time_stamp"]=data[i].time_stamp
            data_dict["latitude"]=float(data[i].latitude)
            data_dict["longitude"]=float(data[i].longitude)
            data_dict["image_path"]=data[i].image_path
            data_dict["predicted_class"]=data[i].predicted_class
            data_dict["crop_type"]=data[i].crop_type
            data_dict["user_name"]=data[i].user_name.user_name
            response_data.append(data_dict)


    context={"response_data":response_data}
    print(context)
    return render(request, 'developer/home.html', context)
    
        