import {Network} from "../../model/api/network";
import axios from 'axios';
import { Component, createSignal } from "solid-js";

const Net: Component= () => {
  const [textStore,setText] = createSignal<string>(); 
  async function getTopTen(){
    try {
      const {data,status}= await axios.get<Network[]>(
        'api/getNetworks',
        {
          headers: {
            Accept: 'application/json',
          },
        },
      );
    
    
    console.log(data);
    // 👇️ "response status is: 200"
    getString(data);
    }catch (error) {
      if (axios.isAxiosError(error)) {
        console.log('error message: ', error.message);
        return error.message;
      } else {
        console.log('unexpected error: ', error);
        return 'An unexpected error occurred';
      }
    }
    console.log(textStore());
  }
  function getString(data : Network[]){
    var output:string="AVERAGE_RATING;NETWORK;TOP_RATED_SHOW;TOP_RATING;SHOW_COUNT\n";
    if (data?.length !== 0){
      for(let i=0;i<data.length;i++){
        output=output.concat(data[i].averageRating+";"+data[i].networkName+";"+data[i].topRatedShow+";"+data[i].topRating+";"+data[i].showCount+"\n");
      }
    }
    console.log(output);
    setText(output);

  }

  function download(){
    const a = document.createElement('a') // Create "a" element
    const blob = new Blob([textStore() as string], {type: "text/plain"}) // Create a blob (file-like object)
    const url = URL.createObjectURL(blob) // Create an object URL from blob
    a.setAttribute('href', url) // Set "a" element link
    a.setAttribute('download', "networks") // Set download filename
    a.click() // Start downloading
  }
  return (
    <div>

        <button onClick={getTopTen}> Get Networks!</button>
        <button onClick={download} >Download Networks</button>
    </div>
  )


}
export default Net;
